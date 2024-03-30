package server_builder.request

import server_builder.serialize.IMessage
import server_builder.serialize.message.Message1
import server_builder.serialize.message.Message2
import server_logic.mapper.GamePackage
import server_logic.mapper.ServerPackage
import java.util.concurrent.locks.ReentrantLock

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


// Пример использования
fun main() {
    val message1 = Message1("Hello, World!")
    val serializedMessage1 = message1.serialize()

    val message2 = Message2(42)
    val serializedMessage2 = message2.serialize()

    val deserializedMessage1 = IMessage.deserialize(serializedMessage1)
    val deserializedMessage2 = IMessage.deserialize(serializedMessage2)

    //println(deserializedMessage1) // Message1(content=Hello, World!)
    //println(deserializedMessage2) // Message2(value=42)
}

