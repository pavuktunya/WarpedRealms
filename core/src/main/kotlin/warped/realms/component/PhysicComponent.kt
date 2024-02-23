package warped.realms.component

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.ui.Image
import ktx.box2d.BodyDefinition
import ktx.box2d.body
import ktx.math.vec2
import warped.realms.entity.Entity
import Component

@Component
class PhysicComponent() {
    val prevPos = vec2()
    val impulse = vec2()
    lateinit var body: Body

    fun onAdded(entity: Entity, component: PhysicComponent = this) {
        component.body.userData = entity
    }
    fun onRemoved(entity: Entity, component: PhysicComponent = this) {
        val body = component.body
        body.world.destroyBody(body)
        body.userData = null
    }
    companion object {
        fun physicCmpFromImage(
            world: World,
            image: Image,
            bodyType: BodyType,
            fixtureAction: BodyDefinition.(PhysicComponent, Float, Float) -> Unit
        ): PhysicComponent {
            return PhysicComponent().also {
                it.body = world.body(bodyType) {
                    position.set(vec2(image.x + image.width * 0.5f, image.y + image.height * 0.5f))
                    fixedRotation = true
                    allowSleep = false
                    this.fixtureAction(it, image.width, image.height)
                }
            }
        }
    }
}
