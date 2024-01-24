package warped.realms.world

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import warped.realms.event.MapChangeEvent
import warped.realms.input.PlayerKeyboardInputProcessor
import warped.realms.server.ServerRequest
import warped.realms.server.request.IGetRequest
import warped.realms.server.request.ISetRequest
import warped.realms.system.*
import warped.realms.test.server.TestServer

class System(val phWorld: World, val inputProcessor: PlayerKeyboardInputProcessor) {
    private val textureAtlas = TextureAtlas("graphics/gameObject.atlas".toInternalFile())
    val physicSystem = PhysicSystem(phWorld)
    val renderSystem = RenderSystem(
        Stage(ExtendViewport(16f, 9f, 1920f, 1080f))
    )
    val moveSystem = MoveSystem()
    private val debugSystem: DebugSystem = DebugSystem(phWorld, renderSystem.stage)
    val animationSystem = AnimationSystem(textureAtlas)
    private val spawnSystem = SpawnSystem(this, textureAtlas, phWorld)
    private val titledMap = TmxMapLoader().load("map/map_1.tmx")

    val cameraSystem = CameraSystem(renderSystem.stage.camera)

    private val eventMapChange = MapChangeEvent(titledMap, renderSystem, spawnSystem, cameraSystem)
    private val eventSystem = EventSystem(eventMapChange)

    private val serverRequest = ServerRequest(
        object : IGetRequest {
            override val setRequest: ISetRequest
                get() = TODO("Not yet implemented")

            override fun getData(pushRequest: ISetRequest): Int {
                return 26
            }
        },
        object : ISetRequest {
            override val getRequest: IGetRequest
                get() = TODO("Not yet implemented")

            override fun sendData(data: Int, getRequest: IGetRequest) {}
        }
    )

    fun getIteratingSystem(): Array<IteratingSystem> = arrayOf(
        moveSystem,
        physicSystem,
        cameraSystem,
        renderSystem,
        animationSystem,
        debugSystem
    )
    fun show() {
        eventSystem.onTick()
    }

    fun resize(width: Int, height: Int) {
        renderSystem.resize(width, height)
    }

    fun dispose() {
        serverRequest.dispose()
        textureAtlas.disposeSafely()
        titledMap.disposeSafely()
    }
}
