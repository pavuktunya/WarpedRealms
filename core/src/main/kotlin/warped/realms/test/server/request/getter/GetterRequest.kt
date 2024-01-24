package warped.realms.test.server.request.getter

import warped.realms.server.request.setter.ISetRequest

class GetterRequest(
    setRequest: ISetRequest
) : IGetRequest {
    override val setRequest: ISetRequest = setRequest
    override fun getData(pushRequest: ISetRequest): Int {
        println("data get")
        return 10
    }
}
