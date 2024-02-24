package warped.realms.factory

import Factory
import warped.realms.component.PhysicComponent
import warped.realms.component.TransformComponent

@Factory(TransformComponent::class)
class TransformFactory {
    fun Factory(lambda: () -> TransformComponent): TransformComponent {
        return lambda.invoke()
    }

    fun Delete(cmp: TransformComponent) {

    }
}
