package warped.realms.component

import Component

@Component
data class EntityComponent(
    val imageComponent: ImageComponent,
    val transformComponent: TransformComponent,
    val animationComponent: AnimationComponent
)
