package warped.realms.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import ktx.assets.disposeSafely
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger
import warped.realms.component.ImageComponent
import warped.realms.event.Event
import warped.realms.event.IHandleEvent
import warped.realms.event.MapChangeEvent
import warped.realms.screen.Screen.Companion.UNIT_SCALE

class RenderSystem(
    val stage: Stage
): IteratingSystem(), IHandleEvent {
    private var images = mutableListOf<ImageComponent>()

    private val bgdLayers = mutableListOf<TiledMapTileLayer>()
    private val fgdLayers = mutableListOf<TiledMapTileLayer>()
    private val mapRenderer = OrthogonalTiledMapRenderer(null, UNIT_SCALE, stage.batch)
    private val orthoCam = stage.camera as OrthographicCamera

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
        mapRenderer.disposeSafely()
    }

    override fun handle(event: Event): Boolean {
        when(event){
            is MapChangeEvent -> {
                bgdLayers.clear()
                fgdLayers.clear()
                event.mapLoader.layers.forEach { layer ->

                    if(layer !is (TiledMapTileLayer)) return false

                    if(layer.name.startsWith("fgd_")){
                        fgdLayers.add(layer)
                    }
                    else if(layer.name.startsWith("bgd_")){
                        bgdLayers.add(layer)
                    }
                    else {
                        logger.debug { "Not found layer in switch construction: ${layer.name}" }
                    }
                }
                return true;
            }
        }
        return false;
    }

    fun Stage.renderTileLayer(list: MutableList<TiledMapTileLayer>){
        if(list.isNotEmpty()){
            stage.batch.use(orthoCam.combined){
                list.forEach {
                    mapRenderer.renderTileLayer(it)
                }
            }
        }
    }

    override fun onTick(deltaTime: Float) {
        super.onTick(deltaTime)

        with(stage){
            viewport.apply()

            AnimatedTiledMapTile.updateAnimationBaseTime()
            mapRenderer.setView(orthoCam)
            //mapRenderer.render()

            renderTileLayer(bgdLayers)

            act(deltaTime)
            draw()

            renderTileLayer(fgdLayers)
        }
    }

    companion object{
        private val logger = logger<RenderSystem>()
    }
}
