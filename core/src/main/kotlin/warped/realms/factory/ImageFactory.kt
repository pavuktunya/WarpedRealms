package warped.realms.factory

import Factory
import com.badlogic.gdx.scenes.scene2d.ui.Image
import warped.realms.component.AnimationComponent
import warped.realms.component.ImageComponent
import kotlin.reflect.KClass

@Factory(ImageComponent::class)
class ImageFactory {
    fun Factory(lambda: () -> ImageComponent): ImageComponent {
        return lambda.invoke()
    }

    fun Delete(cmp: ImageComponent) {

    }
}