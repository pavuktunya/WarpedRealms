package warped.realms.server.request

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import ktx.log.logger
import warped.realms.server.request.getter.GetterRequest
import warped.realms.server.request.getter.IGetRequest
import warped.realms.server.request.setter.ISetRequest
import warped.realms.server.request.setter.SetterRequest
import warped.realms.test.queue.ServerQueue
import java.util.concurrent.locks.ReentrantLock

class ServerRequest() {
    val serverQueue: ServerQueue = ServerQueue()
    val clientQueue: ServerQueue = ServerQueue()
    val client = HttpClient{
        install(WebSockets)
    }

    private val setterRequest: ISetRequest = SetterRequest(clientQueue)
    private val getterRequest: IGetRequest = GetterRequest(serverQueue)

    val lock = ReentrantLock()

    //
//    val got = Thread {
//    руддщ цщкдв
//    }
    val send = Thread {
        runBlocking{
            client.webSocket(HttpMethod.Get, host = "127.0.0.1", port = 8000, path = "/gate") {
                var cord = 0
                while (lock.isLocked) {
                    val message = "[CLIENT]-Try Get $cord + ${java.time.LocalTime.now()}"
                    send(message)
                    cord++
                    delay(1000L)
                }
            }
        }
    }

    init {
        lock.lock()

        got.start()
        send.start()
    }

    fun dispose() {
        lock.unlock()
        serverQueue.stackEmptyCondition.signalAll()
        got.join()
        clientQueue.stackFullCondition.signalAll()
        send.join()
    }

    companion object {
        val log = logger<ServerRequest>()
    }
}
