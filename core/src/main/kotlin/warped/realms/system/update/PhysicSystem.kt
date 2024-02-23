package warped.realms.system.update

import PutComponent
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.World
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2
import warped.realms.component.ImageComponent
import warped.realms.component.PhysicComponent
import System
import Update
import ktx.box2d.createWorld
import ktx.math.vec2
import warped.realms.system.Logger
import warped.realms.system.debug
import warped.realms.system.error

@System
@Update(10)
@PutComponent(PhysicComponent::class, ImageComponent::class)
class PhysicSystem {
    private val physCmps: MutableMap<PhysicComponent, ImageComponent> = mutableMapOf()

    fun Update(deltaTime: Float) {
//        val x = this.javaClass.getAnnotation(Update::class.java)?.priority
//        println("[UPDATE] ${this::class.simpleName} $x")
        if (phWorld.autoClearForces) {
            Logger.error { "AutoClearForces must be set to false to guarantee a correct physic simulation." }
            phWorld.autoClearForces = false
        }
        phWorld.clearForces()
    }

    fun PutComponent(physCmp: PhysicComponent, imageCmp: ImageComponent) {
        if (!physCmp.impulse.isZero) {
            physCmp.body.applyLinearImpulse(physCmp.impulse, physCmp.body.worldCenter, true)
            physCmp.impulse.setZero()
        }
        val vect = physCmp.body.position
        imageCmp.image.run {
            setPosition(vect.x - width * 0.5f, vect.y - height * 0.5f)
        }
        physCmps.put(physCmp, imageCmp)
        println("[DEBUG] Put component ${physCmp::class.simpleName} in ${this::class.simpleName}")
    }
    fun DeleteComponent(component: PhysicComponent) {
        println("[DEBUG] Delete component ${component::class.simpleName} in ${this::class.simpleName}")
    }
    fun DeleteComponent(component: ImageComponent) {
        println("[DEBUG] Delete component ${component::class.simpleName} in ${this::class.simpleName}")
    }
    //Чтобы нагнать разницу в рендере и физики, вызывается несколько раз подряд
    fun onUpdate(deltaTime: Float) {
        phWorld.step(deltaTime, 6, 2)
        physCmps.forEach { physicCmp, imageCmp ->
            physicCmp.prevPos.set(physicCmp.body.position)

            if (!physicCmp.impulse.isZero) {
                physicCmp.body.applyLinearImpulse(physicCmp.impulse, physicCmp.body.worldCenter, true)
                physicCmp.impulse.setZero()
            }
        }
    }
    fun onAlpha(alpha: Float) {
        physCmps.forEach { physicCmp, imageCmp ->
            val (prevX, prevY) = physicCmp.prevPos
            val (bodyX, bodyY) = physicCmp.body.position
            imageCmp.image.run {
                setPosition(
                    MathUtils.lerp(prevX, bodyX, alpha) - width * 0.5f,
                    MathUtils.lerp(prevY, bodyY, alpha) - height * 0.5f
                )
            }
        }
    }
    fun Dispose() {
        println("[DISPOSE] ${this::class.simpleName}")
    }
    companion object {
        val phWorld: World = createWorld(gravity = vec2()).apply {
            setAutoClearForces(false)
        }
    }
}
