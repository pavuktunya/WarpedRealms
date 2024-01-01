package warped.realms.screen

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.log.logger
import warped.realms.WarpedRealms
import warped.realms.component.AnimationComponent
import warped.realms.component.AnimationModel
import warped.realms.component.AnimationType
import warped.realms.component.ImageComponent
import warped.realms.event.MapChangeEvent
import warped.realms.system.AnimationSystem
import warped.realms.system.EventSystem
import warped.realms.system.RenderSystem
import warped.realms.world.World

class Screen(game: WarpedRealms): AScreen(game) {
    //private val texturePlayer = Texture("graphics/fantazy_warrior.png".toInternalFile(), true).apply { Texture.TextureFilter.Linear }
    private val textureSlime = Texture("graphics/monster/rat/idle_00.png".toInternalFile(), true).apply { Texture.TextureFilter.Linear }

    private val textureAtlas = TextureAtlas("graphics/gameObject.atlas".toInternalFile())

    private val renderSystem = RenderSystem(
        Stage(ExtendViewport(16f, 9f, 1920f, 1080f))
    )

    private val animationComponent= AnimationComponent()

    private val imageComponent: ImageComponent = renderSystem.addActor(TextureRegion(textureAtlas.findRegion("fantazy_warrior/run"), 0,0,128,128), scale = 5f)

    private val animationSystem = AnimationSystem(textureAtlas, animationComponent, imageComponent)


    private val titledMap = TmxMapLoader().load("map/map_1.tmx")

    private val eventMapChange = MapChangeEvent(titledMap, renderSystem)

    private val eventSystem = EventSystem(eventMapChange)


    private val world = World(renderSystem,animationSystem)

    override fun show() {
        super.show()
        log.debug{"Game screen shown"}

        eventSystem.onTick()

        renderSystem.addActor(TextureRegion(textureSlime, 64,64), positionX = 14f, scale = 3f)

        animationComponent.nextAnimation(AnimationModel.FANTAZY_WARRIOR, AnimationType.IDLE)
    }
    override fun render(delta: Float) {
        super.render(delta)
        world.onTick(delta)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        renderSystem.resize(width, height)
    }

    override fun dispose() {
        super.dispose()
        world.dispose()
        //texturePlayer.disposeSafely()
        textureSlime.disposeSafely()
        textureAtlas.disposeSafely()
        titledMap.disposeSafely()
    }

    companion object{
        private val log = logger<Screen>()
    }
}
