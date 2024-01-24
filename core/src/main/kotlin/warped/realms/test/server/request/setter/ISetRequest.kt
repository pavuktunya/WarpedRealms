package warped.realms.test.server.request.setter

import warped.realms.server.request.getter.IGetRequest

interface ISetRequest {
    val getRequest: IGetRequest
    abstract fun sendData(data: Int, getRequest: IGetRequest)
}
