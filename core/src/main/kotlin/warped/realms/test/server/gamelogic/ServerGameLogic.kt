package warped.realms.test.server.gamelogic

import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.math.vec2

class ServerGameLogic {
    private val phWorld = createWorld(gravity = vec2()).apply {
        setAutoClearForces(false)
    }
    fun onTick(delta: Float) {
    }
    fun dispose() {
        phWorld.disposeSafely()
    }
}
