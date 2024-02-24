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
import warped.realms.event.Event
import warped.realms.event.IHandleEvent
import warped.realms.event.MapChangeEvent
import warped.realms.screen.Screen.Companion.UNIT_SCALE
import System
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import generated.systems.injectSys
import ktx.box2d.BodyDefinition
import ktx.box2d.body
import warped.realms.entity.GameEntity
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

    private fun spawnCfg(type: entityType, posX: Float, posY: Float): Entity = cachedEntity.getOrPut(type) {
        when (type) {
            entityType.PLAYER -> createEntity(
                AnimationModel.FANTAZY_WARRIOR,
                posX,
                posY,
                size(AnimationModel.FANTAZY_WARRIOR),
                8f
            ).apply {
                input(this)
            }.also { Logger.debug { "Player has spawned with size: ${size(AnimationModel.FANTAZY_WARRIOR)}!" } }

            entityType.RAT -> createEntity(AnimationModel.RAT, posX, posY, size(AnimationModel.RAT), 5f)
                .also { Logger.debug { "Enemy has spawned with size: ${size(AnimationModel.RAT)}!" } }
        }
    }
    override fun handle(event: Event): Boolean {
        when(event){
            is MapChangeEvent -> {
                event.mapLoader.layers.forEach { layer ->
                    if(layer.name == entityLayer){
                        layer.objects.forEach { obj ->
                            spawnCfg(
                                entityType.valueOf(obj.name.uppercase()),
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
    private fun input(gameEntity: GameEntity) {
        injectSys<PlayerKeyboardInputProcessor>().addMoveCmp(gameEntity.getCmp<MoveComponent>())
        injectSys<CameraSystem>().addTrecker(gameEntity.getCmp<ImageComponent>())
    }

    private fun createEntity(
        model: AnimationModel,
        cordX: Float = 1f,
        cordY: Float = 1f,
        size: Vector2,
        speed: Float,
        width: Float = 1f * size.x,
        height: Float = 1f * size.y,
    ): GameEntity {
        val texture: TextureRegion = TextureRegion(
            textureAtlas.findRegion("${model.atlasKey}/idle"),
            0,
            0,
            128,
            128
        )
        val image = ImageComponent(
            Image(texture).apply {
                setPosition(cordX, cordY)
                setSize(width, height)
                setScaling(Scaling.fill)
            }
        )
        return GameEntity(
            {
                image
            }, {
                AnimationComponent().apply {
                    this.nextAnimation(
                        model,
                        AnimationType.IDLE
                    )
                }
            }, {
                TransformComponent(vec2(cordX, cordY))
            }, {
                physicCmpFromImage(
                    phWorld,
                    image.image,
                    BodyDef.BodyType.DynamicBody
                ) { phCmp, width, height ->
                    box(width, height) {
                        isSensor = false
                    }
                }.apply { this.body?.userData = this }
            }, {
                MoveComponent(speed)
            }
        )
    }

    fun physicCmpFromImage(
        world: World,
        image: Image,
        bodyType: BodyDef.BodyType,
        fixtureAction: BodyDefinition.(PhysicComponent, Float, Float) -> Unit
    ): PhysicComponent {
        lateinit var bodyDefinition: BodyDefinition
        return PhysicComponent(
            body = world.body(bodyType) {
                position.set(vec2(image.x + image.width * 0.5f, image.y + image.height * 0.5f))
                fixedRotation = true
                allowSleep = false
                bodyDefinition = this
            }
        ).apply { bodyDefinition.fixtureAction(this, image.width, image.height) }
    }
    fun Dispose() {
        println("[DISPOSE] ${this::class.simpleName}")
    }
}
enum class entityType {
    PLAYER, RAT;

    val atlasKey: String = this.toString().lowercase()
}
