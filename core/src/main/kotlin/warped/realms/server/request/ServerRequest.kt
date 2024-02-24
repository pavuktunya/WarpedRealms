package warped.realms.server.request

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import ktx.log.logger
import java.util.concurrent.locks.ReentrantLock

class ServerRequest() {
//    val serverQueue: ServerQueue = ServerQueue()
//    val clientQueue: ServerQueue = ServerQueue()
    private val gotClient = HttpClient{
        install(WebSockets)
    }
    private val sendClient = HttpClient{
        install(WebSockets)
    }

//    private val setterRequest: ISetRequest = SetterRequest(clientQueue)
//    private val getterRequest: IGetRequest = GetterRequest(serverQueue)

    private val lock = ReentrantLock()

    private val got = Thread {
        runBlocking {
            val objectMapper = jacksonObjectMapper()
            gotClient.webSocket(method = HttpMethod.Get, port = 8080, host = "0.0.0.0", path = "/connection") {
                for (frame in incoming) {
                    frame as? Frame.Binary ?: continue
                    val someData = objectMapper.readValue<SomeData>(frame.readBytes())
                    println(someData)
                    delay(250L)
                }
            }
        }
    }
    private val send = Thread {
        runBlocking {
            val someData = SomeData("sdsadkasdhasdjashdasd", 4324, 12321)
            val objectMapper = jacksonObjectMapper()
            sendClient.webSocket(method = HttpMethod.Put, port = 8080, host = "0.0.0.0", path = "/connection") {
                while (true) {
                    val someDataBinary = objectMapper.writeValueAsBytes(someData)
                    send(Frame.Binary(true, someDataBinary))
                    delay(250L)
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
        //serverQueue.stackEmptyCondition.signalAll()
        got.join()
        //clientQueue.stackFullCondition.signalAll()
        send.join()
    }

    companion object {
        val log = logger<ServerRequest>()
    }
}
