package warped.realms.server.request

class SetterRequest(
    getRequest: IGetRequest
) : ISetRequest {
    override val getRequest: IGetRequest = getRequest
    override fun sendData(data: Int, getRequest: IGetRequest) {
        println("send $data")
    }
}
