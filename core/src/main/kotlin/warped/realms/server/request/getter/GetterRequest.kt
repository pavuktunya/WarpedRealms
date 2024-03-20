package warped.realms.server.request.getter

import warped.realms.test.queue.ThreadSafeQueue

class GetterRequest(
    private val queue: ThreadSafeQueue
) : IGetRequest {
    private var d: Int? = 0
    override fun getData(): Int? {
        d = queue.pop()
        println("[CLIENT] Get $d + ${java.time.LocalTime.now()}")
        return d
    }
}
