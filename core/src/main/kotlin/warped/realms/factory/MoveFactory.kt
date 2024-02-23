package warped.realms.factory

import Factory
import warped.realms.component.AnimationComponent
import warped.realms.component.MoveComponent
import kotlin.reflect.KClass

@Factory(MoveComponent::class)
class MoveFactory {
    fun Factory(lambda: () -> MoveComponent): MoveComponent {
        return MoveComponent()
    }

    fun Delete(cmp: MoveComponent) {

    }
}
