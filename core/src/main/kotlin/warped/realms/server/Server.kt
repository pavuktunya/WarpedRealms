package warped.realms.server

import warped.realms.server.request.ServerRequest
import warped.realms.server.request.getter.IGetRequest
import warped.realms.server.request.setter.ISetRequest

class Server {
    val serverRequest = ServerRequest(
        object : IGetRequest {
            override val setRequest: ISetRequest
                get() = TODO("Not yet implemented")

            override fun getData(pushRequest: ISetRequest): Int {
                return 26
            }
        },
        object : ISetRequest {
            override val getRequest: IGetRequest
                get() = TODO("Not yet implemented")

            override fun sendData(data: Int, getRequest: IGetRequest) {}
        }
    )

    fun dispose() {
        serverRequest.dispose()
    }
}
