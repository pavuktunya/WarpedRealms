package server_builder.serialize.message

import server_builder.serialize.IMessage
import server_builder.serialize.MessageType
import java.nio.ByteBuffer

// Пример класса для первого типа сообщения
data class Message1(val content: String) : IMessage {
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
