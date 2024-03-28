package org.example.request.setter

import org.example.test.queue.ServerQueue

class SetterRequest(
    val queue: ServerQueue
) : ISetRequest {
    override fun sendData(data: Int) {
        queue.push(data)
        println("[CLIENT] Send $data + ${java.time.LocalTime.now()}")
    }
}
