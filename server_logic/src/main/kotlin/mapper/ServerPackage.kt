package server_logic.mapper

import java.io.*

class ServerPackage {
}

// Пример класса состояния игрока с собственной реализацией Serializable
data class PlayerState(var playerId: Int, var health: Int, var positionX: Double, var positionY: Double) :
    Serializable {
    companion object {
        private const val serialVersionUID: Long = 123456789L
    }

    // Метод для сериализации объекта
    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.writeInt(playerId)
        out.writeInt(health)
        out.writeDouble(positionX)
        out.writeDouble(positionY)
    }

    // Метод для десериализации объекта
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(input: ObjectInputStream) {
        playerId = input.readInt()
        health = input.readInt()
        positionX = input.readDouble()
        positionY = input.readDouble()
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObjectNoData() {
        playerId = 0
        health = 0
        positionX = 0.0
        positionY = 0.0
    }
}

// Пример метода для десериализации массива байтов в объект PlayerState
fun deserializePlayerStateFromByteArray(byteArray: ByteArray): PlayerState {
    val byteArrayInputStream = ByteArrayInputStream(byteArray)
    val objectInputStream = ObjectInputStream(byteArrayInputStream)
    val playerState = objectInputStream.readObject() as PlayerState
    objectInputStream.close()
    return playerState
}

fun main() {
    // Пример десериализации массива байтов в объект PlayerState
    val byteArray = byteArrayOf(0)/* полученный массив байтов */
    val playerState = deserializePlayerStateFromByteArray(byteArray)
    println(playerState) // Выводим полученный объект PlayerState
}

