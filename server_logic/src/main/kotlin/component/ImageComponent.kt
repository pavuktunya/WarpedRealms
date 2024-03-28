package server_logic.component

import com.badlogic.gdx.scenes.scene2d.ui.Image

class ImageComponent(
    val image: Image
) : Comparable<ImageComponent> {
    override fun compareTo(other: ImageComponent): Int {
        val yDiff = other.image.y.compareTo(image.y)
        return if (yDiff != 0) {
            yDiff
        } else {
            other.image.x.compareTo(image.x)
        }
    }
}
