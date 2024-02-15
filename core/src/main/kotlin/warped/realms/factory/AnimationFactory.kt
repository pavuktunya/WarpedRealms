package warped.realms.factory

import Factory
import warped.realms.component.AnimationComponent
import kotlin.reflect.KClass

@Factory(AnimationComponent::class)
class AnimationFactory {
    fun Factory(p: KClass<AnimationComponent>): AnimationComponent {
        return AnimationComponent()
    }

    fun Delete(cmp: AnimationComponent) {

    }
}
