package server_logic.mapper

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.util.*

class GamePackage {
}

// Класс сущности (Entity)
data class Entity(val id: Int)

// Класс компонента Transform
data class TransformComponent(val entityId: Int, val positionX: Double, val positionY: Double)

// Класс компонента Move
data class MoveComponent(val entityId: Int, val velocityX: Double, val velocityY: Double)

// Класс компонента Animation
data class AnimationComponent(val entityId: Int, val animationType: String)

// Класс сообщения
data class Message(
    val content: String,
    val date: Date,
    val version: String,
    val clientId: Int,
    val entity: Entity? = null
)

// Класс хранилища всех версий сообщений
class MessageHistory(private val historySize: Int = 1000) {
    private val messageHistory = Array<Message?>(historySize) { null }
    private var currentIndex = 0

    // Метод для добавления сообщения в историю
    fun addMessage(message: Message) {
        currentIndex = (currentIndex + 1) % historySize
        messageHistory[currentIndex] = message
    }

    // Метод для отката состояния на указанное количество минут назад
    fun rollback(minutes: Int): List<Message> {
        val rollbackDate = Calendar.getInstance()
        rollbackDate.add(Calendar.MINUTE, -minutes)
        val rollbackMessages = mutableListOf<Message>()

        var index = currentIndex
        var count = 0
        while (count < historySize) {
            if (messageHistory[index]?.date?.before(rollbackDate.time) == true) {
                break
            }
            messageHistory[index]?.let { rollbackMessages.add(it) }
            index = (index - 1 + historySize) % historySize
            count++
        }

        return rollbackMessages
    }
}

// Класс системы обработки и фильтрации сообщений
class MessageProcessor(private val messageChannel: Channel<Message>, private val messageHistory: MessageHistory) {
    private val filteredChannel = Channel<Message>()

    fun start() {
        GlobalScope.launch {
            for (message in messageChannel) {
                messageHistory.addMessage(message) // Добавляем сообщение в историю
                if (isMessageFiltered(message)) {
                    filteredChannel.send(message)
                }
            }
        }
    }

    private fun isMessageFiltered(message: Message): Boolean {
        // Пример дополнительной фильтрации по типу сущности
        val entityTypeFilter = Entity(1) // Здесь можно указать нужный тип сущности для фильтрации
        return message.entity == entityTypeFilter
    }

    fun getFilteredChannel(): Channel<Message> {
        return filteredChannel
    }
}

// Пример функции для отправки сообщений на обработку
fun sendMessage(message: Message, messageChannel: Channel<Message>) {
    GlobalScope.launch {
        messageChannel.send(message)
    }
}

// Пример функции для отправки отфильтрованных сообщений через WebSocket
fun sendFilteredMessagesOverWebSocket(filteredChannel: Channel<Message>) {
    GlobalScope.launch {
        for (message in filteredChannel) {
            // Отправка сообщения через WebSocket
            println("Sending filtered message over WebSocket: $message")
        }
    }
}

fun main() {
    val messageChannel = Channel<Message>()
    val messageHistory = MessageHistory()
    val messageProcessor = MessageProcessor(messageChannel, messageHistory)
    val filteredChannel = messageProcessor.getFilteredChannel()

    // Запуск обработчика сообщений
    messageProcessor.start()

    // Запуск отправки отфильтрованных сообщений через WebSocket
    sendFilteredMessagesOverWebSocket(filteredChannel)

    // Пример создания сущности игрока и его компонентов
    val playerEntity = Entity(1)
    val transformComponent = TransformComponent(playerEntity.id, positionX = 0.0, positionY = 0.0)
    val moveComponent = MoveComponent(playerEntity.id, velocityX = 0.0, velocityY = 0.0)
    val animationComponent = AnimationComponent(playerEntity.id, animationType = "idle")

    // Пример создания сообщения об обновлении компонентов игрока
    val message = Message("Player state updated", Date(), "1.0", 1, playerEntity)
    sendMessage(message, messageChannel)

    // Пример отката состояния сервера на 5 минут назад
    val rollbackMessages = messageHistory.rollback(5)
    println("Rollback messages:")
    rollbackMessages.forEach { println(it) }
}
