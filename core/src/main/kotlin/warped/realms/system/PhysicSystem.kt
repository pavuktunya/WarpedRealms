package warped.realms.system

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

@System
@Update(10)
class PhysicSystem {
    val phWorld: World = createWorld(gravity = vec2()).apply {
        setAutoClearForces(false)
    }
    private val physCmps: MutableMap<PhysicComponent, ImageComponent> = mutableMapOf()

    fun Update(deltaTime: Float) {
        if (phWorld.autoClearForces) {
            log.error { "AutoClearForces must be set to false to guarantee a correct physic simulation." }
            phWorld.autoClearForces = false
        }
        phWorld.clearForces()
    }

    fun addPhysicComponent(vararg _physCmps: Pair<PhysicComponent, ImageComponent>) {
        physCmps.putAll(mutableMapOf(*_physCmps).also {
            it.forEach { physicCmp, imageCmp ->

                if (!physicCmp.impulse.isZero) {
                    physicCmp.body.applyLinearImpulse(physicCmp.impulse, physicCmp.body.worldCenter, true)
                    physicCmp.impulse.setZero()
                }

                val vect = physicCmp.body.position
                imageCmp.image.run {
                    setPosition(vect.x - width * 0.5f, vect.y - height * 0.5f)
                }
            }
        })
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
    }

    companion object {
        private val log = logger<PhysicSystem>()
    }

}
