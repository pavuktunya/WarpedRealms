package warped.realms.world

import warped.realms.system.EachFrame
import warped.realms.system.Fixed
import warped.realms.system.IteratingSystem

class World(
    vararg system: IteratingSystem
): IteratingSystem(){
    private var systems:Array<IteratingSystem> = arrayOf(*system)

    fun addSystem(system: IteratingSystem){
        systems.plus(system)
    }
    override fun onTick(deltaTime: Float) {
        super.onTick(deltaTime)
        systems.forEach {
            it.onTick(deltaTime)
        }
    }

    override fun dispose() {
        super.dispose()
        systems.forEach {
            it.dispose()
        }
    }
}
