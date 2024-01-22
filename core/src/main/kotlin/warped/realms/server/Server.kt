package warped.realms.server

import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.math.vec2

class Server {
    private val phWorld = createWorld(gravity = vec2()).apply {
        setAutoClearForces(false)
    }

    fun dispose() {
        phWorld.disposeSafely()
    }
}
