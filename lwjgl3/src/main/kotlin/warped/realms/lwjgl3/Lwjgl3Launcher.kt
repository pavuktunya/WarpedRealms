@file:JvmName("Lwjgl3Launcher")

package warped.realms.lwjgl3

import com.badlogic.gdx.Graphics.DisplayMode
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import warped.realms.WarpedRealms

/** Launches the desktop (LWJGL3) application. */
fun main() {
    // This handles macOS support and helps on Windows.
    if (StartupHelper.startNewJvmIfRequired())
      return
    Lwjgl3Application(WarpedRealms(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("WarpedRealms:LootLabyrinth")
        setWindowedMode(1920, 1080)
        //setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode())
        //setResizable(false)
        useVsync(false)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
