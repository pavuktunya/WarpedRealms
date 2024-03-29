package warped.realms.screen

import com.badlogic.gdx.maps.tiled.TmxMapLoader
import generated.systems.Systems
import generated.systems.injectSys
import server_connector.ServerConnector
import warped.realms.WarpedRealms
import warped.realms.event.MapChangeEvent
import warped.realms.system.update.CollisionSpawnSystem
import warped.realms.system.update.CameraSystem
import warped.realms.system.Logger
import warped.realms.system.update.RenderSystem
import warped.realms.system.SpawnSystem
import warped.realms.system.debug
import warped.realms.system.update.mapper.ServerDismapperSystem
import warped.realms.system.update.mapper.ServerMapperSystem

class Screen(game: WarpedRealms): AScreen(game) {
    private val titledMap = TmxMapLoader().load("map/map_1.tmx")
    private val systems = Systems
    private val serverConnector = game.serverConnector.also {
        injectSys<ServerDismapperSystem>().serverConnector = it
        injectSys<ServerMapperSystem>().serverConnector = it
    }

    private val mapChangeEvent = MapChangeEvent(
        titledMap,
        injectSys<RenderSystem>(),
        injectSys<SpawnSystem>(),
        injectSys<CameraSystem>(),
        injectSys<CollisionSpawnSystem>()
    )
    override fun show() {
        super.show()
        Logger.debug { "Game screen shown" }
        mapChangeEvent.onTick()
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
    val renderSystem = injectSys<RenderSystem>()
    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        renderSystem.resize(width, height)
    }
    override fun dispose() {
        systems.Dispose()
        super.dispose()
    }
    companion object{
        const val UNIT_SCALE = 1 / 24f
    }
}
