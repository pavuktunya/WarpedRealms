package warped.realms.system

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.assets.disposeSafely

class DebugSystem(
    private val phWorld: World,
    private val stage: Stage,
    private val physicRenderer: Box2DDebugRenderer = Box2DDebugRenderer()
) : IteratingSystem() {
    override fun onTick(deltaTime: Float) {
        super.onTick(deltaTime)
        physicRenderer.render(phWorld, stage.camera.combined)
    }

    override fun dispose() {
        super.dispose()
        physicRenderer.disposeSafely()
    }
}
