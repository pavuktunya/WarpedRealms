package warped.realms.system.update

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.assets.disposeSafely
import System
import Update

@System
@Update(1)
class DebugSystem {
    private val phWorld: World = PhysicSystem.phWorld
    private val stage: Stage = RenderSystem.stage

    private val physicRenderer: Box2DDebugRenderer = Box2DDebugRenderer()
    fun Update(deltaTime: Float) {
//        val x = this.javaClass.getAnnotation(Update::class.java)?.priority
//        println("[UPDATE] ${this::class.simpleName} $x")

        physicRenderer.render(phWorld, stage.camera.combined)
    }

    fun Dispose() {
        println("[DISPOSE] ${this::class.simpleName}")
        physicRenderer.disposeSafely()
    }
}
