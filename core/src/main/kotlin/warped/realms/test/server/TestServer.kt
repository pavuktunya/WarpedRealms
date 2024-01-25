package warped.realms.test.server

import kotlinx.coroutines.Runnable
import warped.realms.test.queue.ServerQueue
import warped.realms.test.server.gamelogic.ServerGameLogic
import warped.realms.test.server.request.ClientRequest
import warped.realms.world.IDispose
import java.lang.Thread.sleep
import java.util.concurrent.Executors
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class TestServer(serverQueue: ServerQueue, clientQueue: ServerQueue) {
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
    lateinit var clientRequest: ClientRequest
    override fun run() {
        clientRequest = ClientRequest(serverQueue, clientQueue, lock)
    }
}
