package warped.realms.system

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import ktx.assets.disposeSafely
import warped.realms.component.ImageComponent

class RenderSystem(
    private val stage: Stage
): IteratingSystem() {
    private var images = mutableListOf<ImageComponent>()
    fun addActor(
        texture: TextureRegion,
        scale: Float = 1f,
        positionX: Float = 1f,
        positionY: Float = 1f,
        width: Float = 1f*scale,
        height: Float = 1f*scale,

        imageComponent: ImageComponent = ImageComponent(Image(texture).apply {
            setPosition(positionX,positionY)
            setSize(width, height)
            setScaling(Scaling.fill)
        })
    ): ImageComponent
    {
        stage.addActor(
            imageComponent.image
        )
        images.add(imageComponent)
        images = images.sorted().toMutableList()

        images.forEach { it.image.toFront() }

        return imageComponent
    }

    fun removeActor(component: ImageComponent){
        stage.root.removeActor(component.image)
    }

    fun resize(width: Int, height: Int){
        stage.viewport.update(width,height, true)
    }

    override fun dispose(){
        stage.disposeSafely()
    }

    override fun onTick(deltaTime: Float) {
        super.onTick(deltaTime)

        with(stage){
            viewport.apply()
            act(deltaTime)
            draw()
        }
    }
}
