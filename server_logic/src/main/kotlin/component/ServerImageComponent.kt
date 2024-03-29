package server_logic.component

import com.badlogic.gdx.scenes.scene2d.ui.Image

class ServerImageComponent(
    val image: Image
) : Comparable<ServerImageComponent> {
    override fun compareTo(other: ServerImageComponent): Int {
        val yDiff = other.image.y.compareTo(image.y)
        return if (yDiff != 0) {
            yDiff
        } else {
            other.image.x.compareTo(image.x)
        }
    }
}
