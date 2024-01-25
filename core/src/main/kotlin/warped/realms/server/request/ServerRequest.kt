package warped.realms.server.request

import ktx.log.logger
import warped.realms.server.request.getter.GetterRequest
import warped.realms.server.request.getter.IGetRequest
import warped.realms.server.request.setter.ISetRequest
import warped.realms.server.request.setter.SetterRequest
import warped.realms.test.queue.ServerQueue
import java.util.concurrent.locks.ReentrantLock

class ServerRequest() {
    val serverQueue: ServerQueue = ServerQueue()
    val clientQueue: ServerQueue = ServerQueue()

    private val setterRequest: ISetRequest = SetterRequest(clientQueue)
    private val getterRequest: IGetRequest = GetterRequest(clientQueue)

    val lock = ReentrantLock()

    val got = Thread {
        var cord: Int = 0
        while (lock.isLocked) {
            println("[CLIENT]-Try Get $cord + ${java.time.LocalTime.now()}")
            cord = getterRequest.getData()
            cord++
            Thread.sleep(1200)
        }
    }
    val send = Thread {
        var data: Int = 110
        while (lock.isLocked) {
            data++
            println("[CLIENT]-Try Send $data + ${java.time.LocalTime.now()}")
            setterRequest.sendData(data)
            Thread.sleep(1200)
        }
    }

    init {
        lock.lock()

        got.start()
        send.start()
    }

    fun dispose() {
        lock.unlock()

        got.join(5)
        send.join(5)

        if (got.isAlive) {
            clientQueue.stackEmptyCondition.signalAll()
            serverQueue.stackEmptyCondition.signalAll()
        }
        if (send.isAlive) {
            clientQueue.stackFullCondition.signalAll()
            serverQueue.stackFullCondition.signalAll()
        }

        got.join()
        send.join()
    }

    companion object {
        val log = logger<ServerRequest>()
    }
}
