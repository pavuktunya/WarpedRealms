package warped.realms.server

import warped.realms.server.request.ServerRequest
import warped.realms.server.request.getter.IGetRequest
import warped.realms.server.request.setter.ISetRequest
import java.util.concurrent.locks.ReentrantLock

class Server() {
    val serverRequest = ServerRequest()

    fun dispose() {
        serverRequest.dispose()
    }
}
