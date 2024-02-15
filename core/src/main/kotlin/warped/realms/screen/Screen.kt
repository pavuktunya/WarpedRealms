package warped.realms.screen

import com.badlogic.gdx.maps.tiled.TmxMapLoader
import generated.systems.Systems
import generated.systems.injectSys
import ktx.log.logger
import warped.realms.WarpedRealms
import warped.realms.event.MapChangeEvent
import warped.realms.server.Server
import warped.realms.system.CameraSystem
import warped.realms.system.EventSystem
import warped.realms.system.RenderSystem
import warped.realms.system.SpawnSystem
import warped.realms.test.server.TestServer

class Screen(game: WarpedRealms): AScreen(game) {
    //    private val serverHandler = Server()
//    private val server = TestServer(
//        serverHandler.serverRequest.serverQueue,
//        serverHandler.serverRequest.clientQueue
//    )
    private val titledMap = TmxMapLoader().load("map/map_1.tmx")
    private val eventMapChange = MapChangeEvent(
        titledMap,
        injectSys<RenderSystem>(),
        injectSys<SpawnSystem>(),
        injectSys<CameraSystem>()
    ).also {
        injectSys<EventSystem>().addEvent(it)
    }
    private val systems = Systems
    override fun show() {
        super.show()
        log.debug{"Game screen shown"}
        injectSys<EventSystem>().onTick()
    }
    override fun render(delta: Float) {
        super.render(delta)
        systems.Update(delta)
    }

    //    var accumulator: Float = 0f
//    fun onTick(deltaTime: Float){
//        /*
//        when (interval) {
//            is Fixed -> {
//                accumulator += deltaTime
//                val stepRate = interval.step
//
//                if (accumulator < stepRate) {
//                    return
//                }
//
//                while (accumulator >= stepRate) {
//                    onUpdate(deltaTime)
//                    accumulator -= stepRate
//                }
//                onAlpha(accumulator / stepRate)
//            }
//        }
//        */
    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        injectSys<RenderSystem>().resize(width, height)
    }
    override fun dispose() {
        super.dispose()
        systems.Dispose()
    }
    companion object{
        private val log = logger<Screen>()
        const val UNIT_SCALE = 1 / 24f
    }
}
