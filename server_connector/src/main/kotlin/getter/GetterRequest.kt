package server_connector.getter

import server_connector.queue.ServerQueue

class GetterRequest(
    val queue: ServerQueue
) : IGetRequest {
    var d: Int = 0
    override fun getData(): Int {
        d = queue.pop()
        println("[CLIENT] Get $d + ${java.time.LocalTime.now()}")
        return d
    }
}
