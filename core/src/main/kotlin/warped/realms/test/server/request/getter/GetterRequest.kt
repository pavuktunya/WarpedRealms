package warped.realms.test.server.request.getter

import warped.realms.test.queue.ServerQueue

class GetterRequest(
    val queue: ServerQueue
) : IGetRequest {
    override fun getData(): Int {
        val data = queue.pop()
        println("[CLIENT] Get $data + ${java.time.LocalTime.now()}")
        return data
    }
}
