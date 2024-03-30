package server_connector.serialize.message

import server_connector.serialize.IMessage
import server_connector.serialize.MessageType
import java.nio.ByteBuffer

// Пример класса для второго типа сообщения
data class Message2(val value: Int) : IMessage {
    override fun serialize(): ByteArray {
        val buffer = ByteBuffer.allocate(8)
        buffer.put(MessageType.MESSAGE_TYPE_2.value)
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
