package warped.realms.test.server.request

import ktx.log.logger
import warped.realms.test.queue.ServerQueue
import warped.realms.test.server.request.getter.GetterRequest
import warped.realms.test.server.request.getter.IGetRequest
import warped.realms.test.server.request.setter.ISetRequest
import warped.realms.test.server.request.setter.SetterRequest

class ClientRequest(serverQueue: ServerQueue, clientQueue: ServerQueue) {
    private val setterRequest: ISetRequest = SetterRequest(serverQueue)
    private val getterRequest: IGetRequest = GetterRequest(clientQueue)

    var flag = true

    val t1 = Thread {
        var data: Int = 10
        while (flag) {
            println("[SERVER]-Try Get $data + ${java.time.LocalTime.now()}")
            data = getterRequest.getData()
            Thread.sleep(1200)
        }
    }
    val t2 = Thread {
        var data: Int = 10
        while (flag) {
            println("[SERVER]-Try Send $data + ${java.time.LocalTime.now()}")
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
    }

    companion object {
        val log = logger<ClientRequest>()
    }
}
