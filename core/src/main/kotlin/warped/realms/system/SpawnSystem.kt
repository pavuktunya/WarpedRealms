package warped.realms.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import ktx.app.gdxError
import ktx.box2d.box
import ktx.math.vec2
import ktx.tiled.x
import ktx.tiled.y
import warped.realms.component.*
import warped.realms.entity.Entity
import warped.realms.entity.PlayerEntity
import warped.realms.event.Event
import warped.realms.event.IHandleEvent
import warped.realms.event.MapChangeEvent
import warped.realms.screen.Screen.Companion.UNIT_SCALE
import System
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import generated.systems.createCmp
import generated.systems.injectSys
import warped.realms.input.PlayerKeyboardInputProcessor
import warped.realms.system.update.CameraSystem
import warped.realms.system.update.PhysicSystem
import warped.realms.system.update.RenderSystem

const val entityLayer = "entities"

@System
class SpawnSystem : IHandleEvent {
    private val textureAtlas: TextureAtlas = RenderSystem.textureAtlas
    private val phWorld: World = PhysicSystem.phWorld

    private val cachedEntity = mutableMapOf<entityType, Entity>()
    private val cachedSizes = mutableMapOf<AnimationModel, Vector2>()

    //    private fun spawnCfg(type: entityType, posX: Float, posY: Float): Entity = cachedEntity.getOrPut(type) {
//        when (type) {
//            entityType.PLAYER -> createPlayerEntity("fantazy_warrior", posX, posY, size(AnimationModel.FANTAZY_WARRIOR)).apply {
//                entityComponent.animationComponent.nextAnimation(
//                    AnimationModel.FANTAZY_WARRIOR, AnimationType.IDLE
//                )
//            }.also { Logger.debug { "Player has spawned with size: ${size(AnimationModel.FANTAZY_WARRIOR)}!" } }
//
//            entityType.RAT -> createEnemyEntity("rat", posX, posY, size(AnimationModel.RAT)).apply {
//                entityComponent.animationComponent.nextAnimation(
//                    AnimationModel.RAT, AnimationType.IDLE
//                )
//            }.also { Logger.debug { "Enemy has spawned with size: ${size(AnimationModel.RAT)}!" } }
//
//            else -> gdxError("Type $type has not defined!")
//        }
//    }
    override fun handle(event: Event): Boolean {
        when(event){
            is MapChangeEvent -> {
                event.mapLoader.layers.forEach { layer ->
                    if(layer.name == entityLayer){
                        layer.objects.forEach { obj ->
//                            spawnCfg(
//                                entityType.valueOf(obj.name.uppercase()),
//                                obj.x * UNIT_SCALE,
//                                obj.y * UNIT_SCALE
//                            )
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
        //system.animationSystem.addAnimationComponent(entity.entityComponent.animationComponent to entity.entityComponent.imageComponent)
    }
    private fun phys(physicComponent: PhysicComponent, imageComponent: ImageComponent) {
        //system.physicSystem.addPhysicComponent(physicComponent to imageComponent)
    }
    private fun move(moveComponent: MoveComponent, physicComponent: PhysicComponent) {
        //system.moveSystem.addMoveComponent(moveComponent to physicComponent)
    }
    private fun input(imageComponent: ImageComponent, moveComponent: MoveComponent) {
        //system.inputProcessor.addMoveCmp(moveComponent)
        injectSys<PlayerKeyboardInputProcessor>().addMoveCmp(moveComponent)
        injectSys<CameraSystem>().addTrecker(imageComponent)
    }
    private fun createEntity(name: String, cordX: Float = 0f, cordY: Float = 0f, size: Vector2): Entity {
        val animationComponent = createCmp {
            AnimationComponent()
        }
        val imageComponent: ImageComponent = createCmp<ImageComponent> {
            val texture: TextureRegion = TextureRegion(textureAtlas.findRegion("$name/idle"), 0, 0, 128, 128)
            ImageComponent(Image(texture).apply {
                setPosition(cordX, cordY)
                setSize(width, height)
                setScaling(Scaling.fill)
            })
        }
        return Entity(
//            imageComponent,
//            TransformComponent(vec2(cordX, cordY)),
//            animationComponent
        ).also {
            spawn(it)
        }
    }
    fun Dispose() {
        println("[DISPOSE] ${this::class.simpleName}")
    }
}

enum class entityType {
    PLAYER, RAT;

    val atlasKey: String = this.toString().lowercase()
}
