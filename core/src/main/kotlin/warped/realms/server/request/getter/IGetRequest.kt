package warped.realms.server.request.getter

import warped.realms.server.request.setter.ISetRequest

interface IGetRequest {
    fun getData(): Int?
}
