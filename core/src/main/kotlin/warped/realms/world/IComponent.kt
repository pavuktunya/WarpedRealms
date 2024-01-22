package warped.realms.world

import warped.realms.component.Component
import warped.realms.component.LivingComponent
import warped.realms.component.TransformComponent

interface IComponent<out T : Component> {
    val component: T
}

interface ILivingComponent {
    val livingComponent: LivingComponent
}

