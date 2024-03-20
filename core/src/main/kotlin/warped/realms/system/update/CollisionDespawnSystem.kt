package warped.realms.system.update

import System
import PutComponent
import Update
import generated.systems.injectSys
import warped.realms.component.TiledComponent
import warped.realms.event.CollisionDespawnEvent

@System
@Update(9)
@PutComponent(TiledComponent::class)
class CollisionDespawnSystem {
    private val tiledCmps = mutableListOf<TiledComponent>()
    val collDespEvent: CollisionDespawnEvent by lazy {
        CollisionDespawnEvent(injectSys<CollisionSpawnSystem>())
    }
    fun Update(delta: Float) {
        tiledCmps.forEach { tiledCmp ->
            if (tiledCmp.nearbyEntities.isEmpty()) {
                collDespEvent.cell = tiledCmp.cell
                collDespEvent.onTick()
                tiledCmp.isActive = false
                //deleteCmp<TiledComponent>(tiledCmp)
            }
        }
    }
    fun PutComponent(tileCmp: TiledComponent) {
        tiledCmps.add(tileCmp)
    }
    fun DeleteComponent(tileCmp: TiledComponent) {
        tiledCmps.remove(tileCmp)
    }
    fun Dispose() {

    }
}
