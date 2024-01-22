package warped.realms.system

import com.badlogic.gdx.physics.box2d.World
import ktx.log.logger
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

    override fun onUpdate(deltaTime: Float) {
        super.onUpdate(deltaTime)
        phWorld.step(deltaTime, 6, 2)
    }

    override fun dispose() {
        super.dispose()
    }

    companion object {
        private val log = logger<PhysicSystem>()
    }

}
