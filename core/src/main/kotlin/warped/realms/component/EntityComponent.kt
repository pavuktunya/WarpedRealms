package warped.realms.component

import warped.realms.component.AnimationComponent
import warped.realms.component.ImageComponent
import warped.realms.component.TransformComponent
import warped.realms.world.IDispose

data class EntityComponent(
    private val imageComponent: ImageComponent,
    private val transformComponent: TransformComponent,
    private val animationComponent: AnimationComponent
): IDispose{
    override fun dispose() {
    }
}
