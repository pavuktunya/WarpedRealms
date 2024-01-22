package warped.realms.entity

import ktx.math.vec2
import warped.realms.component.AnimationComponent
import warped.realms.component.ImageComponent
import warped.realms.component.LivingComponent
import warped.realms.component.TransformComponent
import warped.realms.world.*

class PlayerEntity(
    imageComponent: ImageComponent,
    transformComponent: TransformComponent,
    animationComponent: AnimationComponent,
): Entity(
    imageComponent,
    transformComponent,
    animationComponent
), ILivingComponent {
    override val livingComponent = LivingComponent(100f, 100f)
}
