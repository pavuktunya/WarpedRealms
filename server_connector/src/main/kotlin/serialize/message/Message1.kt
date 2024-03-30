package server_connector.serialize.message

import server_connector.serialize.IMessage
import server_connector.serialize.MessageType
import java.nio.ByteBuffer

// Пример класса для первого типа сообщения
data class Message1(val content: String) : IMessage {
    override fun serialize(): ByteArray {
        val contentBytes = content.toByteArray()
        val buffer = ByteBuffer.allocate(4 + contentBytes.size)
        buffer.put(MessageType.MESSAGE_TYPE_1.value)
        buffer.put(contentBytes)
        return buffer.array()
    }

    companion object {
        fun deserialize(bytes: ByteArray): IMessage {
            val contentBytes = bytes.sliceArray(4 until bytes.size)
            return Message1(String(contentBytes))
        }
    }
}
