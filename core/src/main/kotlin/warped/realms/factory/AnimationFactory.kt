package warped.realms.factory

import Factory
import warped.realms.component.AnimationComponent
import warped.realms.component.ImageComponent
import java.util.ArrayList
import kotlin.reflect.KClass

@Factory(AnimationComponent::class)
class AnimationFactory {
    fun Factory(lambda: () -> AnimationComponent): AnimationComponent {
        return AnimationComponent()
    }

    fun Delete(cmp: AnimationComponent) {

    }
}
