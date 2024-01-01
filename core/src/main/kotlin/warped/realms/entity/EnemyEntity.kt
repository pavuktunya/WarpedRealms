package warped.realms.entity

import warped.realms.component.AnimationComponent
import warped.realms.component.ImageComponent
import warped.realms.component.TransformComponent
import warped.realms.world.IDispose

class EnemyEntity(
    imageComponent: ImageComponent,
    transformComponent: TransformComponent,
    animationComponent: AnimationComponent,
): Entity(
    imageComponent,
    transformComponent,
    animationComponent
) {

}
