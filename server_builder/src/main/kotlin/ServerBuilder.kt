package server_builder

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
    init {
        lock.lock()
        println("==========Server Built==========")
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
