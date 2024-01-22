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
import warped.realms.system.SpawnSystem
import warped.realms.world.System
import warped.realms.world.World

class Screen(game: WarpedRealms): AScreen(game) {
    private val system: System = System()
    private val world = World(*system.getIteratingSystem())

    override fun show() {
        super.show()
        log.debug{"Game screen shown"}
        system.show()
    }
    override fun render(delta: Float) {
        super.render(delta)
        world.onTick(delta)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        system.resize(width, height)
    }

    override fun dispose() {
        super.dispose()
        system.dispose()
        world.dispose()
    }

    companion object{
        private val log = logger<Screen>()
        const val UNIT_SCALE = 1 / 24f
    }
}
