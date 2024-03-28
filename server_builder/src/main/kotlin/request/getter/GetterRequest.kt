package server_builder.request.getter

import server_builder.queue.ServerQueue

class GetterRequest(
    val queue: ServerQueue
) : IGetRequest {
    override fun getData(): Int {
        val d = queue.pop()
        println("[CLIENT] Get $d + ${java.time.LocalTime.now()}")
        return d
    }
}
