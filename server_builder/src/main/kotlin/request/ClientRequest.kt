package server_builder.request

import server_logic.mapper.GamePackage
import server_logic.mapper.ServerPackage
import java.util.concurrent.locks.ReentrantLock
import java.nio.ByteBuffer

class ClientRequest() {
    private val lock = ReentrantLock()

    //Симуляция потоков websocket, именно здесь должна быть логика пересылки сообщений
    val threadGet = Thread {
        var data: Int = 0
        while (lock.isLocked) {
            println("[SERVER]-Try Get $data + ${java.time.LocalTime.now()}")
            //data = getterRequest.getData()
            data++
            Thread.sleep(500)
        }
    }
    val threadSend = Thread {
        var data: Int = 100
        while (lock.isLocked) {
            data++
            println("[SERVER]-Try Send $data + ${java.time.LocalTime.now()}")
            //setterRequest.sendData(data)
            Thread.sleep(1000)
        }
    }

    fun getPackage(): ServerPackage {
        return ServerPackage()
    }

    fun sendPackage(gamePackage: GamePackage) {

    }
    fun startConnection() {
        lock.lock()
        threadGet.start()
        threadSend.start()
    }

    fun endConnection() {
        lock.unlock()
        threadGet.join()
        threadSend.join()
    }
}

// Интерфейс для всех типов игровых сообщений
interface GameMessage {
    fun serialize(): ByteArray

    companion object {
        fun deserialize(bytes: ByteArray): GameMessage {
            val buffer = ByteBuffer.wrap(bytes)
            val type = buffer.int
            return when (type) {
                MessageType.MESSAGE_TYPE_1.value -> Message1.deserialize(bytes)
                MessageType.MESSAGE_TYPE_2.value -> Message2.deserialize(bytes)
                // Добавляем обработку других типов сообщений по аналогии
                else -> throw IllegalArgumentException("Unknown message type: $type")
            }
        }
    }
}

// Пример enum с типами сообщений
enum class MessageType(val value: Int) {
    MESSAGE_TYPE_1(1),
    MESSAGE_TYPE_2(2),
    // Добавляем другие типы сообщений по мере необходимости
}

// Пример класса для первого типа сообщения
data class Message1(val content: String) : GameMessage {
    override fun serialize(): ByteArray {
        val contentBytes = content.toByteArray()
        val buffer = ByteBuffer.allocate(4 + contentBytes.size)
        buffer.putInt(MessageType.MESSAGE_TYPE_1.value)
        buffer.put(contentBytes)
        return buffer.array()
    }

    companion object {
        fun deserialize(bytes: ByteArray): Message1 {
            val contentBytes = bytes.sliceArray(4 until bytes.size)
            return Message1(String(contentBytes))
        }
    }
}

// Пример класса для второго типа сообщения
data class Message2(val value: Int) : GameMessage {
    override fun serialize(): ByteArray {
        val buffer = ByteBuffer.allocate(8)
        buffer.putInt(MessageType.MESSAGE_TYPE_2.value)
        buffer.putInt(value)
        return buffer.array()
    }

    companion object {
        fun deserialize(bytes: ByteArray): Message2 {
            val buffer = ByteBuffer.wrap(bytes)
            buffer.int // Skip message type
            val value = buffer.int
            return Message2(value)
        }
    }
}

// Пример использования
fun main() {
    val message1 = Message1("Hello, World!")
    val serializedMessage1 = message1.serialize()

    val message2 = Message2(42)
    val serializedMessage2 = message2.serialize()

    val deserializedMessage1 = GameMessage.deserialize(serializedMessage1)
    val deserializedMessage2 = GameMessage.deserialize(serializedMessage2)

    println(deserializedMessage1) // Message1(content=Hello, World!)
    println(deserializedMessage2) // Message2(value=42)
}

