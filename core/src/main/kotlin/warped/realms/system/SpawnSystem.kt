package warped.realms.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import ktx.app.gdxError
import ktx.log.Logger
import ktx.log.debug
import ktx.log.logger
import ktx.math.vec2
import warped.realms.component.*
import warped.realms.entity.EnemyEntity
import warped.realms.entity.Entity
import warped.realms.entity.PlayerEntity
import warped.realms.event.Event
import warped.realms.event.IHandleEvent
import warped.realms.event.MapChangeEvent
import warped.realms.screen.Screen.Companion.UNIT_SCALE
import warped.realms.world.System

const val entityLayer = "entities"

class SpawnSystem(val system: System, val textureAtlas: TextureAtlas) : IteratingSystem(), IHandleEvent {

    private val cachedEntity = mutableMapOf<String, Entity>()
    private val cachedSizes = mutableMapOf<AnimationModel, Vector2>()

    private fun spawnCfg(type: String, posX: Float, posY: Float): Entity = cachedEntity.getOrPut(type) {
        when (type) {
            "player" -> createEntity("fantazy_warrior", posX, posY, size(AnimationModel.FANTAZY_WARRIOR)).apply {
                entityComponent.animationComponent.nextAnimation(
                    AnimationModel.FANTAZY_WARRIOR, AnimationType.IDLE
                )
            }.also { log.debug { "Player has spawned with size: ${size(AnimationModel.FANTAZY_WARRIOR)}!" } }

            "rat" -> createEntity("rat", posX, posY, size(AnimationModel.RAT)).apply {
                entityComponent.animationComponent.nextAnimation(
                    AnimationModel.RAT, AnimationType.IDLE
                )
            }.also { log.debug { "Enemy has spawned with size: ${size(AnimationModel.RAT)}!" } }

            else -> gdxError("Type $type has not defined!")
        }
    }
    override fun handle(event: Event): Boolean {
        when(event){
            is MapChangeEvent -> {
                event.mapLoader.layers.forEach { layer ->
                    if(layer.name == entityLayer){
                        layer.objects.forEach { obj ->
                            spawnCfg(
                                obj.name,
                                obj.properties.get("x").toString().toFloat() * UNIT_SCALE,
                                obj.properties.get("y").toString().toFloat() * UNIT_SCALE
                            )
                        }
                        return true;
                    }
                }
            }
        }
        return false
    }

    private fun size(model: AnimationModel) = cachedSizes.getOrPut(model) {
        val regions = textureAtlas.findRegions("${model.atlasKey}/${AnimationType.IDLE.atlasKey}")
        if (regions.isEmpty) {
            gdxError("There are no regions for the idle animation of the model $model")
        }
        val firstFrame = regions.first()
        vec2(firstFrame.originalWidth * UNIT_SCALE, firstFrame.originalHeight * UNIT_SCALE)
    }

    private fun spawn(entity: Entity) {
        system.animationSystem.addAnimationComponent(entity.entityComponent.animationComponent to entity.entityComponent.imageComponent)
    }

    private fun createEntity(name: String, cordX: Float = 0f, cordY: Float = 0f, size: Vector2): Entity {
        val animationComponent = AnimationComponent()
        val imageComponent: ImageComponent = system.renderSystem.addActor(
            TextureRegion(textureAtlas.findRegion("$name/idle"), 0, 0, 128, 128),
            positionX = cordX,
            positionY = cordY,
            width = size.x,
            height = size.y
        )
        return Entity(
            imageComponent,
            TransformComponent(vec2(cordX, cordY)),
            animationComponent
        ).also { spawn(it) }
    }


    override fun onTick(deltaTime: Float) {
        super.onTick(deltaTime)
    }
    override fun dispose() {
        super.dispose()
    }

    companion object {
        private val log = logger<SpawnSystem>()
    }
}
