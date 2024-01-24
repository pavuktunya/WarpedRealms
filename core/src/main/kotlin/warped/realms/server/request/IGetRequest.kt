package warped.realms.server.request

interface IGetRequest {
    val setRequest: ISetRequest
    abstract fun getData(pushRequest: ISetRequest): Int
}
