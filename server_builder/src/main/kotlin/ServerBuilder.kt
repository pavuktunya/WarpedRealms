package server_builder

import server_builder.queue.ServerQueue
import server_builder.request.ServerRequest
import server_logic.ServerGameLogic
import java.lang.Thread.sleep
import java.util.concurrent.locks.ReentrantLock

//Сборка сервера, методы подключения к нему
class ServerBuilder() {
    val lock = ReentrantLock()

    private val t0: Thread = Thread {
        val serverGameLogic = ServerGameLogic()

        val fixedTime: Int = 1000 * 1 / 60
        var deltaTime: Int = 1000 * 1 / 10

        while (lock.isLocked) {
            serverGameLogic.onTick((deltaTime / fixedTime).toFloat())
            sleep(1200)
        }
        serverGameLogic.dispose()
    }

    //private val t1 = Request(serverQueue, clientQueue, lock)

    init {
        lock.lock()
        println("Server Builder")
        //t0.start()
        //t1.start()
    }

    fun dispose() {
        lock.unlock()
        //t0.join()

        //t1.clientRequest.dispose()
        //t1.join()
    }
}

class Request(
    val serverQueue: ServerQueue,
    val clientQueue: ServerQueue,
    val lock: ReentrantLock
) : Thread() {
    lateinit var clientRequest: ServerRequest
    override fun run() {
        clientRequest = ServerRequest(serverQueue, clientQueue, lock)
    }
}
