package warped.realms.system.update.mapper

import Update
import System
import server_connector.ServerConnector
import warped.realms.component.ImageComponent
import warped.realms.entity.mapper.EntityMapper

import java.util.*


@System
@Update(11)
class ServerMapperSystem {
    private val entityMappers = mutableListOf<EntityMapper>()
    lateinit var serverConnector: ServerConnector

    fun Update(deltaTime: Float) {
        entityMappers.forEach { it.Update() }
        serverConnector.push(entityMappers.push())
    }
    fun PutComponent(cmp: EntityMapper) {
        entityMappers.add(cmp)
    }

    fun Dispose() {
        entityMappers.clear()
    }
    fun MutableList<EntityMapper>.push(): ByteArray {
        val b = byteArrayOf(0, 0, 0, 0, 0)
        return b
    }
}

// Класс сущности (Entity)
data class Entity(val id: Int)

// Класс компонента Transform
data class TransformComponent(val entityId: Int, val positionX: Double, val positionY: Double)

// Класс компонента Move
data class MoveComponent(val entityId: Int, val velocityX: Double, val velocityY: Double)

// Класс компонента Animation
data class AnimationComponent(val entityId: Int, val animationType: String)

// Снимок игрового состояния
data class GameStateSnapshot(
    val timestamp: Date,
    val entities: List<Entity>,
    val transforms: List<TransformComponent>,
    val moves: List<MoveComponent>,
    val animations: List<AnimationComponent>
)

// Хранилище снимков
class SnapshotStorage(private val maxSnapshots: Int = 10) {
    private val snapshots = mutableListOf<GameStateSnapshot>()

    // Метод сохранения снимка
    fun saveSnapshot(snapshot: GameStateSnapshot) {
        if (snapshots.size >= maxSnapshots) {
            snapshots.removeAt(0) // Удаляем самый старый снимок, если достигнут предел
        }
        snapshots.add(snapshot)
    }

    // Метод для отката игры на указанное количество минут назад
    fun rollback(minutes: Int): GameStateSnapshot? {
        val rollbackDate = Date(
            java.time.LocalTime.now().toNanoOfDay() - minutes * 60 * 1000
        ) // Вычисляем временную отметку для отката
        return snapshots.lastOrNull { it.timestamp <= rollbackDate } // Возвращаем последний снимок, сделанный до указанного времени
    }
}

fun main() {
    // Пример использования хранилища снимков
    val snapshotStorage = SnapshotStorage()

    // Создаем несколько сущностей и их компонентов
    val entities = listOf(
        Entity(1),
        Entity(2),
        Entity(3)
    )
    val transforms = entities.map { TransformComponent(it.id, 0.0, 0.0) }
    val moves = entities.map { MoveComponent(it.id, 0.0, 0.0) }
    val animations = entities.map { AnimationComponent(it.id, "idle") }

    // Создаем снимок текущего состояния игры
    val currentSnapshot = GameStateSnapshot(Date(), entities, transforms, moves, animations)

    // Сохраняем снимок в хранилище
    snapshotStorage.saveSnapshot(currentSnapshot)

    // Пример отката игры на 5 минут назад
    val rollbackSnapshot = snapshotStorage.rollback(5)
    if (rollbackSnapshot != null) {
        println("Rolling back to snapshot from ${rollbackSnapshot.timestamp}")
        // Восстанавливаем состояние игры до состояния на момент снимка
        // rollbackSnapshot.entities, rollbackSnapshot.transforms и т.д.
    } else {
        println("No snapshot found for rollback")
    }
}

