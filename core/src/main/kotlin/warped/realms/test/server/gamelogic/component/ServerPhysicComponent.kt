package warped.realms.test.server.gamelogic.component

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.ui.Image
import ktx.box2d.BodyDefinition
import ktx.box2d.body
import ktx.math.vec2
import warped.realms.entity.Entity

class ServerPhysicComponent() {
    lateinit var body: Body
    fun onAdded(entity: Entity, component: ServerPhysicComponent = this) {
        component.body.userData = entity
    }

    fun onRemoved(entity: Entity, component: ServerPhysicComponent = this) {
        val body = component.body
        body.world.destroyBody(body)
        body.userData = null
    }

    companion object {
        fun physicCmpFromImage(
            world: World,
            image: Image,
            bodyType: BodyType,
            fixtureAction: BodyDefinition.(ServerPhysicComponent, Float, Float) -> Unit
        ): ServerPhysicComponent {
            return ServerPhysicComponent().also {
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
