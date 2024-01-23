package warped.realms.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.World
import ktx.log.debug
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2
import warped.realms.component.AnimationComponent
import warped.realms.component.ImageComponent
import warped.realms.component.PhysicComponent

class PhysicSystem(
    private val phWorld: World,
    vararg physCmps: Pair<PhysicComponent, ImageComponent>
) : IteratingSystem(interval = Fixed(1 / 60f)) {
    private val physCmps: MutableMap<PhysicComponent, ImageComponent> = mutableMapOf(*physCmps)


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

    override fun onTick(deltaTime: Float) {
        super.onTick(deltaTime)
        if (phWorld.autoClearForces) {
            log.error { "AutoClearForces must be set to false to guarantee a correct physic simulation." }
            phWorld.autoClearForces = false
        }
        phWorld.clearForces()
    }

    //Чтобы нагнать разницу в рендере и физики, вызывается несколько раз подряд
    override fun onUpdate(deltaTime: Float) {
        super.onUpdate(deltaTime)
        phWorld.step(deltaTime, 6, 2)
        physCmps.forEach { physicCmp, imageCmp ->
            physicCmp.prevPos.set(physicCmp.body.position)

            if (!physicCmp.impulse.isZero) {
                physicCmp.body.applyLinearImpulse(physicCmp.impulse, physicCmp.body.worldCenter, true)
                physicCmp.impulse.setZero()
            }
        }
    }

    override fun onAlpha(alpha: Float) {
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

    override fun dispose() {
        super.dispose()
    }

    companion object {
        private val log = logger<PhysicSystem>()
    }

}
