package warped.realms.component

import warped.realms.component.AnimationComponent
import warped.realms.component.ImageComponent
import warped.realms.component.TransformComponent
import warped.realms.world.IDispose

data class EntityComponent(
    val imageComponent: ImageComponent,
    val transformComponent: TransformComponent,
    val animationComponent: AnimationComponent
): IDispose{
    override fun dispose() {

    }
}
