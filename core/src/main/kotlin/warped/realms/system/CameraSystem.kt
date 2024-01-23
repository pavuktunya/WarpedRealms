package warped.realms.system

import com.badlogic.gdx.graphics.Camera
import ktx.tiled.height
import ktx.tiled.width
import warped.realms.component.ImageComponent
import warped.realms.entity.PlayerEntity
import warped.realms.event.Event
import warped.realms.event.IHandleEvent
import warped.realms.event.MapChangeEvent

class CameraSystem(
    camera: Camera,
) : IteratingSystem(), IHandleEvent {
    private val camera = camera
    private val imageCmps: MutableList<ImageComponent> = mutableListOf()

    private var maxW = 0f
    private var maxH = 0f

    fun addTrecker(imageComponent: ImageComponent) {
        imageCmps.add(imageComponent)
    }

    override fun onTick(deltaTime: Float) {
        super.onTick(deltaTime)
        val viewW = camera.viewportWidth * 0.5f
        val viewH = camera.viewportHeight * 0.5f
        with(imageCmps.last()) {
            camera.position.set(
                (image.x + image.width * 0.5f).coerceIn(viewW, maxW - viewW),
                (image.y + image.height * 0.5f).coerceIn(viewH, maxH - viewH),
                camera.position.z
            )
        }
    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is MapChangeEvent -> {
                maxW = event.mapLoader.width.toFloat()
                maxH = event.mapLoader.height.toFloat()
                return true
            }
        }
        return false
    }
}
