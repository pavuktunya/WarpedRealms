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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import generated.systems.injectSys
import ktx.box2d.BodyDefinition
import ktx.box2d.body
import warped.realms.entity.GameEntity
import warped.realms.entity.mapper.EntityMapper
import warped.realms.input.PlayerKeyboardInputProcessor
import warped.realms.system.update.CameraSystem
import warped.realms.system.update.PhysicSystem
import warped.realms.system.update.RenderSystem
import warped.realms.system.update.mapper.ServerDismapperSystem
import warped.realms.system.update.mapper.ServerMapperSystem

const val entityLayer = "entities"

@System
class SpawnSystem : IHandleEvent {
    private val textureAtlas: TextureAtlas = RenderSystem.textureAtlas
    private val phWorld: World = PhysicSystem.phWorld

    //private val cachedEntity = mutableMapOf<entityType, Entity>()
    private val cachedSizes = mutableMapOf<AnimationModel, Vector2>()

    private fun spawnCfg(type: entityType, posX: Float, posY: Float): Entity = when (type) {
        entityType.PLAYER -> createEntity(
            AnimationModel.FANTAZY_WARRIOR,
            posX,
            posY,
            size(AnimationModel.FANTAZY_WARRIOR),
            8f,
            physicScaling = vec2(0.3f, 0.3f),
            physicOffset = vec2(0f, -10f * UNIT_SCALE)
        ).apply {
            input(this)
        }.also {
            injectSys<ServerMapperSystem>().PutComponent(EntityMapper(it))
            injectSys<ServerDismapperSystem>().PutComponent(EntityMapper(it))

            Logger.debug { "Player has spawned with size: ${size(AnimationModel.FANTAZY_WARRIOR)}!" }
        }
        entityType.RAT -> createEntity(
            AnimationModel.RAT,
            posX,
            posY,
            size(AnimationModel.RAT),
            5f,
            physicScaling = vec2(0.3f, 0.3f),
            physicOffset = vec2(0f, -2f * UNIT_SCALE)
        ).also { Logger.debug { "Enemy has spawned with size: ${size(AnimationModel.RAT)}!" } }
//        entityType.CHEST -> createEntity(
//            AnimationModel.CHEST,
//            posX,
//            posY,
//            size(AnimationModel.CHEST),
//            5f,
//            physicScaling = vec2(0.3f, 0.3f),
//            physicOffset = vec2(0f, -2f* UNIT_SCALE),
//            BodyType.StaticBody
//        )
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
        physicScaling: Vector2 = vec2(1f, 1f),
        physicOffset: Vector2 = vec2(0f, 0f),
        bodyType: BodyType = BodyType.DynamicBody,
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
        val imgCmp = fun ImageComponent.() = this.apply {
            this.image = Image(texture).apply {
                setPosition(cordX, cordY)
                setSize(width, height)
                setScaling(Scaling.fill)
            }
        }
        val anCmp = fun AnimationComponent.() = this.apply {
            this.nextAnimation(
                model,
                AnimationType.IDLE
            )
        }
        val trCmp = fun TransformComponent.() = this.apply {
            location = vec2(cordX, cordY)
        }
        val physCmp = fun PhysicComponent.() = this.apply {
            val a = physicCmpFromImage(
                phWorld,
                Image(texture).apply {
                    setPosition(cordX, cordY)
                    setSize(width, height)
                    setScaling(Scaling.fill)
                },
                bodyType
            ) { phCmp, width, height ->
                val w = width * physicScaling.x
                val h = height * physicScaling.y
                //hit box
                box(w, h, physicOffset) {
                    isSensor = bodyType != BodyType.StaticBody
                }
                if (bodyType != BodyType.StaticBody) {
                    //collision box
                    val collH = h * 0.4f
                    val collOffset = vec2().apply { set(physicOffset) }
                    collOffset.y -= h * 0.5f - collH * 0.5f
                    //box(w,collH,collOffset)
                    box(w, collH, physicOffset)
                }
            }
            this.a()
                .also { this.body!!.userData = this }
        }
        val mvCmp = fun MoveComponent.() = this.apply {
            this.speed = speed
        }
        //val sum = fun Int.(other: Int): Int = this + other
        val clCmp: (TiledComponent.() -> TiledComponent)? = when (bodyType) {
            BodyType.StaticBody -> fun TiledComponent.() = this
            else -> null
        }
        return GameEntity(
            imgCmp,
            anCmp,
            trCmp,
            physCmp,
            mvCmp,
            clCmp
        )
    }

    fun physicCmpFromImage(
        world: World,
        image: Image,
        bodyType: BodyDef.BodyType,
        fixtureAction: BodyDefinition.(PhysicComponent, Float, Float) -> Unit
    ): PhysicComponent.() -> PhysicComponent {
        lateinit var bodyDefinition: BodyDefinition
        return fun PhysicComponent.() = this.apply {
            body = world.body(bodyType) {
                position.set(vec2(image.x + image.width * 0.5f, image.y + image.height * 0.5f))
                fixedRotation = true
                allowSleep = false
                bodyDefinition = this
                bodyDefinition.fixtureAction(PhysicComponent(), image.width, image.height)
            }
            bodyDefinition.fixtureAction(this, image.width, image.height)
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
