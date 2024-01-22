package warped.realms.world

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import warped.realms.event.MapChangeEvent
import warped.realms.system.*

class System(val phWorld: World) {
    private val textureAtlas = TextureAtlas("graphics/gameObject.atlas".toInternalFile())
    val physicSystem = PhysicSystem(phWorld)
    val renderSystem = RenderSystem(
        Stage(ExtendViewport(16f, 9f, 1920f, 1080f))
    )
    private val debugSystem: DebugSystem = DebugSystem(phWorld, renderSystem.stage)
    val animationSystem = AnimationSystem(textureAtlas)
    private val spawnSystem = SpawnSystem(this, textureAtlas, phWorld)
    private val titledMap = TmxMapLoader().load("map/map_1.tmx")

    private val eventMapChange = MapChangeEvent(titledMap, renderSystem, spawnSystem)
    private val eventSystem = EventSystem(eventMapChange)

    fun getIteratingSystem(): Array<IteratingSystem> = arrayOf(
        physicSystem,
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
        textureAtlas.disposeSafely()
        titledMap.disposeSafely()
    }
}
