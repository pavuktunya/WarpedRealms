package warped.realms.server.request.setter

import warped.realms.test.queue.ThreadSafeQueue

class SetterRequest(
    private val queue: ThreadSafeQueue
) : ISetRequest {
    override fun sendData(data: Int) {
        queue.push(data)
        println("[CLIENT] Send $data + ${java.time.LocalTime.now()}")
    }
}
