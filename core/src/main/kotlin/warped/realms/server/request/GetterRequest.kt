package warped.realms.server.request

class GetterRequest(
    setRequest: ISetRequest
) : IGetRequest {
    override val setRequest: ISetRequest = setRequest
    override fun getData(pushRequest: ISetRequest): Int {
        println("data get")
        return 10
    }
}
