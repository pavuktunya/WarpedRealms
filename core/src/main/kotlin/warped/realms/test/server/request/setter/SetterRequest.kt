package warped.realms.test.server.request.setter

import warped.realms.test.queue.ServerQueue

class SetterRequest(
    val queue: ServerQueue
) : ISetRequest {
    override fun sendData(data: Int) {
        queue.push(data)
        println("[CLIENT] Send $data + ${java.time.LocalTime.now()}")
    }
}
