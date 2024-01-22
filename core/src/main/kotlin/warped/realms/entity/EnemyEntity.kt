package warped.realms.entity

import warped.realms.component.AnimationComponent
import warped.realms.component.ImageComponent
import warped.realms.component.PhysicComponent
import warped.realms.component.TransformComponent

class EnemyEntity(
    physicComponent: PhysicComponent,
    entity: Entity,
    imageComponent: ImageComponent = entity.entityComponent.imageComponent,
    transformComponent: TransformComponent = entity.entityComponent.transformComponent,
    animationComponent: AnimationComponent = entity.entityComponent.animationComponent
): Entity(
    imageComponent,
    transformComponent,
    animationComponent
) {

}
