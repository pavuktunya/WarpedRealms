package warped.realms.server.request.setter

import warped.realms.server.request.getter.IGetRequest

interface ISetRequest {
    fun sendData(data: Int)
}
