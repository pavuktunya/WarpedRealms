package warped.realms.component

import com.badlogic.gdx.math.Vector2
import ktx.math.vec2

data class TransformComponent(
    var location: Vector2 = vec2()
) : Component
