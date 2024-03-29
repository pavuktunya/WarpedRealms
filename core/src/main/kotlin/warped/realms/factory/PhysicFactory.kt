package warped.realms.factory

import Factory
import warped.realms.component.AnimationComponent
import warped.realms.component.PhysicComponent
import kotlin.reflect.KClass

@Factory(PhysicComponent::class)
class PhysicFactory {
    fun Factory(lambda: PhysicComponent.() -> PhysicComponent): PhysicComponent {
        return PhysicComponent().lambda()
    }

    fun Delete(cmp: PhysicComponent) {

    }
}
