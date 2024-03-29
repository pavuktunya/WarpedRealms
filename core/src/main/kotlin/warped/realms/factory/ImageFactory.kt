package warped.realms.factory

import Factory
import com.badlogic.gdx.scenes.scene2d.ui.Image
import warped.realms.component.ImageComponent

@Factory(ImageComponent::class)
class ImageFactory {
    fun Factory(lambda: ImageComponent.() -> ImageComponent): ImageComponent {
        return ImageComponent(Image()).lambda()
    }

    fun Delete(cmp: ImageComponent) {

    }
}
