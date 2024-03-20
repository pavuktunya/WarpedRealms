package warped.realms.system.update

import PutComponent
import System
import Update
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Polyline
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Shape2D
import com.badlogic.gdx.physics.box2d.BodyDef
import generated.systems.createCmp
import ktx.box2d.body
import ktx.box2d.chain
import ktx.box2d.circle
import ktx.box2d.loop
import ktx.collections.GdxArray
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2
import ktx.tiled.*
import warped.realms.component.PhysicComponent
import warped.realms.component.TiledComponent
import warped.realms.event.CollisionDespawnEvent
import warped.realms.event.Event
import warped.realms.event.IHandleEvent
import warped.realms.event.MapChangeEvent
import warped.realms.screen.Screen.Companion.UNIT_SCALE
import warped.realms.system.Logger
import warped.realms.system.debug
import kotlin.math.max

@System
@Update(11)
@PutComponent(TiledComponent::class, PhysicComponent::class)
class CollisionSpawnSystem : IHandleEvent {
    private val phWorld = PhysicSystem.phWorld
    private val tiledMapLayers = GdxArray<TiledMapTileLayer>()
    private val physicCmps = mutableMapOf<PhysicComponent, TiledComponent>()
    private val processedCell = mutableSetOf<Cell>()
    fun PutComponent(tileCmp: TiledComponent, physCmp: PhysicComponent) {
        physicCmps[physCmp] = tileCmp
        val (entityX, entityY) = physCmp.body!!.position
        tiledMapLayers.forEach { layer ->
            layer.forEachCell(entityX.toInt(), entityY.toInt(), SPAWN_AREA_SIZE) { cell, x, y ->
                if (cell.tile.objects.isNotEmpty()) {
                    //cell is not linked to a collision object -> do nothing
                    return@forEachCell
                }
                if (cell in processedCell) {
                    return@forEachCell
                }
                processedCell.add(cell)
                cell.tile.objects.forEach { mapObject ->
                    createCmp<TiledComponent> {
                        TiledComponent().apply {
                            this.cell = cell
                            nearbyEntities.add(tileCmp)
                        }
                    }
                }
            }
        }
    }
    fun Update(delta: Float) {
        physicCmps.forEach { physCmp, collCmp ->
            val (entityX, entityY) = physCmp.body!!.position
            tiledMapLayers.forEach { layer ->
                layer.forEachCell(entityX.toInt(), entityY.toInt(), SPAWN_AREA_SIZE) { cell, x, y ->
                    if (cell.tile.objects.isNotEmpty()) {
                        //cell is not linked to a collision object -> do nothing
                        return@forEachCell
                    }
                    if (cell in processedCell) {
                        return@forEachCell
                    }
                    processedCell.add(cell)
                    cell.tile.objects.forEach { mapObject ->
                        createCmp<TiledComponent> {
                            TiledComponent().apply {
                                this.cell = cell
                                nearbyEntities.add(collCmp)
                            }
                        }
                    }
                }
            }
        }
    }
    fun DeleteComponent(component: PhysicComponent) {
        physicCmps.remove(component)
    }
    fun DeleteComponent(component: TiledComponent) {
        physicCmps.filterValues { it == component }?.keys?.forEach {
            physicCmps.remove(it)
        }
    }
    private fun TiledMapTileLayer.forEachCell(
        startX: Int,
        startY: Int,
        size: Int,
        action: (TiledMapTileLayer.Cell, Int, Int) -> Unit
    ) {
        for (x in startX - size..startX + size) {
            for (y in startY - size..startY + size) {
                this.getCell(x, y)?.let { action(it, x, y) }
            }
        }
    }
    override fun handle(event: Event): Boolean {
        when (event) {
            is MapChangeEvent -> {
                Logger.debug { "Creating collision components..." }
                event.mapLoader.layers.getByType(TiledMapTileLayer::class.java, tiledMapLayers).forEach { layer ->
                    (layer as TiledMapTileLayer).forEachCell(
                        0,
                        0,
                        max(event.mapLoader.width, event.mapLoader.height)
                    ) { cell, x, y ->
                        if (cell.tile.objects.isEmpty()) {
                            //cell is not linked to a collision objects -> do nothing
                            return@forEachCell
                        }
                        cell.tile.objects.forEach { mapObject ->
                            createCollision(x, y, mapObject.shape)
                        }
                    }
                }
                val w = event.mapLoader.width.toFloat()
                val h = event.mapLoader.height.toFloat()
                createPhysCmp(0f, 0f, w, h)
                return true
            }

            is CollisionDespawnEvent -> {
                processedCell.remove(event.cell)
            }
        }
        return false
    }
    private fun createCollision(
        x: Int,
        y: Int,
        shape: Shape2D
    ): PhysicComponent {
        when (shape) {
            is Rectangle -> {
                val bodyX = x + shape.x * UNIT_SCALE
                val bodyY = y + shape.y * UNIT_SCALE

                val bodyW = shape.width * UNIT_SCALE
                val bodyH = shape.height * UNIT_SCALE

                return createPhysCmp(bodyX, bodyY, bodyW, bodyH)
            }

            is Polygon -> {
                val x = shape.x
                val y = shape.y
                return createCmp<PhysicComponent> {
                    PhysicComponent(body = phWorld.body(BodyDef.BodyType.StaticBody) {
                        position.set(x * UNIT_SCALE, y * UNIT_SCALE)
                        shape.setPosition(0f, 0f)
                        shape.setScale(UNIT_SCALE, UNIT_SCALE)
                        loop(shape.transformedVertices)
                        shape.setPosition(x, y)
                    })
                }
            }
            is Polyline -> {
                val x = shape.x
                val y = shape.y
                return createCmp<PhysicComponent> {
                    PhysicComponent(body = phWorld.body(BodyDef.BodyType.StaticBody) {
                        position.set(x * UNIT_SCALE, y * UNIT_SCALE)
                        shape.setPosition(0f, 0f)
                        shape.setScale(UNIT_SCALE, UNIT_SCALE)
                        chain(shape.transformedVertices)
                        shape.setPosition(x, y)
                    })
                }
            }
            else -> error("No such shape in CollisionSpawnSystem")
        }
    }
    fun createPhysCmp(
        bodyX: Float,
        bodyY: Float,
        bodyW: Float,
        bodyH: Float
    ): PhysicComponent {
        return createCmp<PhysicComponent> {
            PhysicComponent(body = phWorld.body(BodyDef.BodyType.StaticBody) {
                position.set(bodyX, bodyY)
                fixedRotation = true
                allowSleep = false
                loop(
                    vec2(0f, 0f),
                    vec2(bodyW, 0f),
                    vec2(bodyW, bodyH),
                    vec2(0f, bodyH),
                )
                circle(SPAWN_AREA_SIZE + 2f) {
                    isSensor = true
                }
            })
        }
    }
    fun Dispose() {

    }
    companion object {
        const val SPAWN_AREA_SIZE = 3
    }
}
