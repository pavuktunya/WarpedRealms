package warped.realms.server.request

interface ISetRequest {
    val getRequest: IGetRequest
    abstract fun sendData(data: Int, getRequest: IGetRequest)
}
