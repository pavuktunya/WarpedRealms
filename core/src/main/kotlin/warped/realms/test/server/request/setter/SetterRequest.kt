package warped.realms.test.server.request.setter

import warped.realms.server.request.getter.IGetRequest

class SetterRequest(
    getRequest: IGetRequest
) : ISetRequest {
    override val getRequest: IGetRequest = getRequest
    override fun sendData(data: Int, getRequest: IGetRequest) {
        println("send $data")
    }
}
