package warped.realms.factory

import Factory
import warped.realms.component.ImageComponent

@Factory(ImageComponent::class)
class ImageFactory {
    fun Factory(lambda: () -> ImageComponent): ImageComponent {
        return lambda.invoke()
    }

    fun Delete(cmp: ImageComponent) {

    }
}
