package warped.realms.server

import ktx.log.logger
import warped.realms.server.request.GetterRequest
import warped.realms.server.request.IGetRequest
import warped.realms.server.request.ISetRequest
import warped.realms.server.request.SetterRequest

class ServerRequest(
    set_subscriber: IGetRequest,
    get_subscriber: ISetRequest
) {
    val setterRequest: ISetRequest = SetterRequest(set_subscriber)
    val getterRequest: IGetRequest = GetterRequest(get_subscriber)

    var flag = true

    val t1 = Thread {
        var data: Int = 0
        while (flag) {
            setterRequest.sendData(data, setterRequest.getRequest)
            println("++ Client: Push Coord")

            Thread.sleep(1200)
            data = getterRequest.getData(getterRequest.setRequest)
            println("++ Client: Get Response: $data")

            Thread.sleep(100)
        }
    }

    init {
        t1.start()
    }

    fun dispose() {
        flag = false
        t1.join()
    }

    companion object {
        val log = logger<ServerRequest>()
    }
}
