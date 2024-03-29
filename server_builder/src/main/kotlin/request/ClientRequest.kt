package server_builder.request

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
            Thread.sleep(1000)
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
