package warped.realms.entity

import warped.realms.component.*
import warped.realms.world.*

class PlayerEntity(
    physicComponent: PhysicComponent,
    entity: Entity,
    imageComponent: ImageComponent = entity.entityComponent.imageComponent,
    transformComponent: TransformComponent = entity.entityComponent.transformComponent,
    animationComponent: AnimationComponent = entity.entityComponent.animationComponent
): Entity(
    imageComponent,
    transformComponent,
    animationComponent
), ILivingComponent {
    override val livingComponent = LivingComponent(100f, 100f)
}
