package warped.realms.server.request.getter

import warped.realms.test.queue.ServerQueue

class GetterRequest(
    val queue: ServerQueue
) : IGetRequest {
    override fun getData(): Int {
        var data = queue.pop()
        println("[CLIENT] Get $data + ${java.time.LocalTime.now()}")
        return data
    }
}
