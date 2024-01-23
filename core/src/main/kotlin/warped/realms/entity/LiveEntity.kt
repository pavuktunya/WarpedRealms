package warped.realms.entity

import warped.realms.component.*

open class LiveEntity(
    val moveComponent: MoveComponent,
    val physicComponent: PhysicComponent,
    entityComponent: EntityComponent,
    imageComponent: ImageComponent = entityComponent.imageComponent,
    transformComponent: TransformComponent = entityComponent.transformComponent,
    animationComponent: AnimationComponent = entityComponent.animationComponent
) : Entity(
    imageComponent,
    transformComponent,
    animationComponent
) {

}
