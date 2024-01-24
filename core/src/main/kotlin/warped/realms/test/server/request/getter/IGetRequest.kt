package warped.realms.test.server.request.getter

import warped.realms.server.request.setter.ISetRequest

interface IGetRequest {
    val setRequest: ISetRequest
    abstract fun getData(pushRequest: ISetRequest): Int
}
