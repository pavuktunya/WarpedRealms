package warped.realms.system

import System
import PutComponent
import Update
import generated.systems.deleteCmp
import generated.systems.injectSys
import warped.realms.component.PhysicComponent
import warped.realms.component.TiledComponent
import warped.realms.event.CollisionDespawnEvent

@System
@Update(9)
@PutComponent(TiledComponent::class)
class CollisionDespawnSystem {
    private val tiledCmps = mutableListOf<TiledComponent>()
    private val collDispEvent = CollisionDespawnEvent(injectSys<CollisionSpawnSystem>())
    fun Update(delta: Float) {
        tiledCmps.forEach { tiledCmp ->
            if (tiledCmp.nearbyEntities.isEmpty()) {
                collDispEvent.cell = tiledCmp.cell
                collDispEvent.onTick()
                deleteCmp<TiledComponent>(tiledCmp)
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
