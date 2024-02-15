package warped.realms.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import ktx.app.gdxError
import ktx.box2d.box
import ktx.log.logger
import ktx.math.vec2
import ktx.tiled.x
import ktx.tiled.y
import warped.realms.component.*
import warped.realms.entity.EnemyEntity
import warped.realms.entity.Entity
import warped.realms.entity.LiveEntity
import warped.realms.entity.PlayerEntity
import warped.realms.event.Event
import warped.realms.event.IHandleEvent
import warped.realms.event.MapChangeEvent
import warped.realms.screen.Screen.Companion.UNIT_SCALE
import System
import generated.systems.createCmp
import generated.systems.injectSys

const val entityLayer = "entities"

@System
class SpawnSystem : IHandleEvent {
    private val textureAtlas: TextureAtlas = RenderSystem.textureAtlas
    private val phWorld: World = PhysicSystem.phWorld

    private val cachedEntity = mutableMapOf<String, Entity>()
    private val cachedSizes = mutableMapOf<AnimationModel, Vector2>()

    private fun spawnCfg(type: String, posX: Float, posY: Float): Entity = cachedEntity.getOrPut(type) {
        when (type) {
            "player" -> createPlayerEntity("fantazy_warrior", posX, posY, size(AnimationModel.FANTAZY_WARRIOR)).apply {
                entityComponent.animationComponent.nextAnimation(
                    AnimationModel.FANTAZY_WARRIOR, AnimationType.IDLE
                )
            }.also { log.debug { "Player has spawned with size: ${size(AnimationModel.FANTAZY_WARRIOR)}!" } }

            "rat" -> createEnemyEntity("rat", posX, posY, size(AnimationModel.RAT)).apply {
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
                                obj.x * UNIT_SCALE,
                                obj.y * UNIT_SCALE
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
        //system.animationSystem.addAnimationComponent(entity.entityComponent.animationComponent to entity.entityComponent.imageComponent)
    }
    private fun phys(physicComponent: PhysicComponent, imageComponent: ImageComponent) {
        //system.physicSystem.addPhysicComponent(physicComponent to imageComponent)
    }

    private fun move(moveComponent: MoveComponent, physicComponent: PhysicComponent) {
        //system.moveSystem.addMoveComponent(moveComponent to physicComponent)
    }
    private fun input(moveComponent: MoveComponent, imageComponent: ImageComponent) {
        //system.inputProcessor.addMoveCmp(moveComponent)
        //system.cameraSystem.addTrecker(imageComponent)
    }

    private fun createPlayerEntity(name: String, cordX: Float = 0f, cordY: Float = 0f, size: Vector2): PlayerEntity {
        return PlayerEntity(createLiveEntity(name, cordX, cordY, size).also {
            it.moveComponent.speed = 5f
            move(it.moveComponent, it.physicComponent)
            input(it.moveComponent, it.entityComponent.imageComponent)
        })
    }

    private fun createEnemyEntity(name: String, cordX: Float = 0f, cordY: Float = 0f, size: Vector2): EnemyEntity {
        return EnemyEntity(createLiveEntity(name, cordX, cordY, size).also {
            it.moveComponent.speed = 8f
            move(it.moveComponent, it.physicComponent)
        })
    }

    private fun createLiveEntity(name: String, cordX: Float = 0f, cordY: Float = 0f, size: Vector2): LiveEntity {
        val entity = createEntity(name, cordX, cordY, size)
        return LiveEntity(
            MoveComponent(1f),
            PhysicComponent.physicCmpFromImage(
            phWorld,
            entity.entityComponent.imageComponent.image,
            BodyDef.BodyType.DynamicBody
        ) { phCmp, width, height ->
            box(width, height) {
                isSensor = false
            }
        }.also {
            it.onAdded(entity)
            phys(it, entity.entityComponent.imageComponent)
            }, entity.entityComponent
        )
    }

    private fun createEntity(name: String, cordX: Float = 0f, cordY: Float = 0f, size: Vector2): Entity {
        val animationComponent = AnimationComponent()
        val imageComponent: ImageComponent = createCmp<ImageComponent>()
//            system.renderSystem.addActor(
//            TextureRegion(textureAtlas.findRegion("$name/idle"), 0, 0, 128, 128),
//            positionX = cordX,
//            positionY = cordY,
//            width = size.x,
//            height = size.y
//        )
        return Entity(
            imageComponent,
            TransformComponent(vec2(cordX, cordY)),
            animationComponent
        ).also { spawn(it) }
    }


    fun onTick(deltaTime: Float) {
    }

    fun Dispose() {
    }

    companion object {
        private val log = logger<SpawnSystem>()
    }
}
