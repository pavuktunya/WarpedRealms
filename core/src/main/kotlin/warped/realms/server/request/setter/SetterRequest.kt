package warped.realms.server.request.setter

import warped.realms.server.request.getter.IGetRequest
import warped.realms.test.queue.ServerQueue
import java.sql.Time
import kotlin.system.measureTimeMillis

class SetterRequest(
    val queue: ServerQueue
) : ISetRequest {
    override fun sendData(data: Int) {
        queue.push(data)
        println("[CLIENT] Send $data + ${java.time.LocalTime.now()}")
    }
}
