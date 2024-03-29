package server_builder

import server_builder.queue.ServerQueue
import server_builder.request.ClientRequest
import server_logic.ServerGameLogic
import server_logic.mapper.GamePackage
import server_logic.mapper.ServerPackage
import java.lang.Thread.sleep
import java.util.concurrent.locks.ReentrantLock

//Сборка сервера, методы подключения к нему
class ServerBuilder() {
    private val lock = ReentrantLock()

    private val clientRequest = ClientRequest()

    private val threadGameLogic: Thread = Thread {
        val serverGameLogic = ServerGameLogic()

        lateinit var gamePackage: GamePackage
        lateinit var serverPackage: ServerPackage

        val fixedTime: Int = 1000 * 1 / 60
        var deltaTime: Int = 1000 * 1 / 10

        while (lock.isLocked) {
            val time_now = java.time.LocalTime.now().nano

            serverPackage = clientRequest.getPackage()
            serverGameLogic.updatePackage(serverPackage)

            gamePackage = serverGameLogic.onTick((1f / fixedTime))
            clientRequest.sendPackage(gamePackage)

            val time_then = java.time.LocalTime.now().nano

            sleep((1f / fixedTime).toLong() - (time_then - time_now).toLong())
        }
        serverGameLogic.dispose()
    }

    init {
        lock.lock()
        println("==========Server Built==========")
        clientRequest.startConnection()
        threadGameLogic.start()
    }

    fun dispose() {
        lock.unlock()
        clientRequest.endConnection()
        threadGameLogic.join()
    }
}
