package server_connector.serialize

// Пример enum с типами сообщений
enum class MessageType(val value: Byte) {
    MESSAGE_TYPE_1(0),
    MESSAGE_TYPE_2(1),
    MESSAGE_TYPE_3(2)
    // Добавляем другие типы сообщений по мере необходимости
}
