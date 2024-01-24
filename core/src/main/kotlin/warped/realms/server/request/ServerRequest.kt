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
    private val getterRequest: IGetRequest = GetterRequest(serverQueue)

    var flag = true

    val t1 = Thread {
        var data: Int = 0
        while (flag) {
            println("[CLIENT]-Try Get $data + ${java.time.LocalTime.now()}")
            data = getterRequest.getData()
            Thread.sleep(1200)
        }
    }
    val t2 = Thread {
        var data: Int = 5
        while (flag) {
            println("[CLIENT]-Try Send $data + ${java.time.LocalTime.now()}")
            setterRequest.sendData(data)
            Thread.sleep(1200)
        }
    }

    init {
        t1.start()
        t2.start()
    }

    fun dispose() {
        flag = false
        t1.join()
        t2.join()
    }

    companion object {
        val log = logger<ServerRequest>()
    }
}
