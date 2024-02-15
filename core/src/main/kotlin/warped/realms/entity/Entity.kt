package warped.realms.entity

import warped.realms.component.AnimationComponent
import warped.realms.component.EntityComponent
import warped.realms.component.ImageComponent
import warped.realms.component.TransformComponent

open class Entity(
    imageComponent: ImageComponent,
    transformComponent: TransformComponent,
    animationComponent: AnimationComponent,
    val entityComponent: EntityComponent = EntityComponent(
        imageComponent,
        transformComponent,
        animationComponent
    )
)
