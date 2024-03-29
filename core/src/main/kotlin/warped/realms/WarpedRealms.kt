package warped.realms

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import ktx.app.KtxGame
import ktx.app.KtxScreen
import warped.realms.screen.Screen

class WarpedRealms : KtxGame<KtxScreen>() {
    //private val camera: OrthographicCamera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(Screen(this))
        setScreen<Screen>()
    }
}
