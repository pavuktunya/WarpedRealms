package warped.realms

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import ktx.app.KtxGame
import ktx.app.KtxScreen
import server_connector.ServerConnector
import warped.realms.screen.Screen

//Игровая логика, работа с server_connector
class WarpedRealms : KtxGame<KtxScreen>() {
    //private val camera: OrthographicCamera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    val serverConnector = ServerConnector()
    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(Screen(this))
        setScreen<Screen>()
    }
    override fun dispose() {
        serverConnector.Dispose()
        super.dispose()
    }
}
