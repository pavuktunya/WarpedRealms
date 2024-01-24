package warped.realms.test.server

import kotlinx.coroutines.Runnable
import warped.realms.test.queue.ServerQueue
import warped.realms.test.server.gamelogic.ServerGameLogic
import warped.realms.test.server.request.ClientRequest
import warped.realms.world.IDispose
import java.lang.Thread.sleep
import java.util.concurrent.Executors
import java.util.concurrent.locks.Lock

class TestServer(serverQueue: ServerQueue, clientQueue: ServerQueue) : IDispose {
    private val t0: Thread
    private val t1: Thread
    private var flag = true

    init {
        t0 = Thread {
            val serverGameLogic = ServerGameLogic()

            val fixedTime: Int = 1000 * 1 / 60
            var deltaTime: Int = 1000 * 1 / 10

            while (flag) {
                serverGameLogic.onTick((deltaTime / fixedTime).toFloat())
                sleep(1200)
            }
            serverGameLogic.dispose()
        }

        t1 = Thread {
            val clientRequest = ClientRequest(serverQueue, clientQueue)
        }

        t0.start()
        t1.start()
    }

    override fun dispose() {
        flag = false
        t1.join()
        t0.join()
    }
}


fun MutableList<Int>.safeAdd(lock: Lock) {
    lock.lock()
    repeat(3) {
        add(it)
        sleep(1000)
    }
    lock.unlock()
}

fun MutableList<Int>.monitor() {
    repeat(6) {
        println(this)
        sleep(500)
    }
}

class Tasks {
    val executor = Executors.newSingleThreadExecutor().apply {
        val executor = this
        repeat(5) {
            val task = RunnableTask()
            executor.execute(task)
        }
        executor.shutdown()
    }
}

class RunnableTask() : Runnable {
    override fun run() {
        repeat(5) {
            println("Task $it")
            sleep(100)
        }
    }
}

class Counter {
    private var count = 0;
    private val lock = Any()

    fun increment() {
        synchronized(lock) {
            count++
        }
    }
}

