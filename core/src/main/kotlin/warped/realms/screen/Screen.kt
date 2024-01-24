package warped.realms.screen


import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.vec2
import warped.realms.WarpedRealms
import warped.realms.input.PlayerKeyboardInputProcessor
import warped.realms.server.Server
import warped.realms.test.server.TestServer
import warped.realms.world.System
import warped.realms.world.World

class Screen(game: WarpedRealms): AScreen(game) {
    private val server = TestServer()
    private val serverHandler = Server()

    private val phWorld = createWorld(gravity = vec2()).apply {
        setAutoClearForces(false)
    }
    private val inputProcessor: PlayerKeyboardInputProcessor = PlayerKeyboardInputProcessor()

    private val system: System = System(phWorld, inputProcessor)
    private val world = World(*system.getIteratingSystem())

    override fun show() {
        super.show()
        log.debug{"Game screen shown"}
        system.show()
    }
    override fun render(delta: Float) {
        super.render(delta)
        world.onTick(delta.coerceAtMost(0.25f))
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        system.resize(width, height)
    }

    override fun dispose() {
        super.dispose()
        server.dispose()
        serverHandler.dispose()

        system.dispose()
        world.dispose()
        phWorld.disposeSafely()
    }

    companion object{
        private val log = logger<Screen>()
        const val UNIT_SCALE = 1 / 24f
    }
}
