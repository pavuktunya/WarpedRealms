package server_builder.request

import server_builder.queue.ServerQueue
import server_builder.request.getter.GetterRequest
import server_builder.request.getter.IGetRequest
import server_builder.request.setter.ISetRequest
import server_builder.request.setter.SetterRequest
import java.util.concurrent.locks.ReentrantLock

class ServerRequest(serverQueue: ServerQueue, clientQueue: ServerQueue, val lock: ReentrantLock) {
    private val setterRequest: ISetRequest = SetterRequest(serverQueue)
    private val getterRequest: IGetRequest = GetterRequest(clientQueue)


    val t1 = Thread {
        var data: Int = 0
        while (lock.isLocked) {
            println("[SERVER]-Try Get $data + ${java.time.LocalTime.now()}")
            data = getterRequest.getData()
            data++
            Thread.sleep(1000)
        }
    }
    val t2 = Thread {
        var data: Int = 100
        while (lock.isLocked) {
            data++
            println("[SERVER]-Try Send $data + ${java.time.LocalTime.now()}")
            setterRequest.sendData(data)
            Thread.sleep(1000)
        }
    }

    init {
        t1.start()
        t2.start()
    }

    fun dispose() {
        t1.join()
        t2.join()
    }
}
