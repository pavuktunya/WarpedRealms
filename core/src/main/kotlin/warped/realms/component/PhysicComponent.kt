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
class PhysicComponent(
    var body: Body? = null
) {
    val prevPos = vec2()
    val impulse = vec2()
}
