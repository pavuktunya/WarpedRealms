package warped.realms.server.request

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import ktx.log.logger
import warped.realms.server.request.getter.GetterRequest
import warped.realms.server.request.setter.SetterRequest
import warped.realms.test.queue.ThreadSafeQueue
import java.lang.Thread.sleep
import java.util.concurrent.locks.ReentrantLock
import kotlin.io.readText

class ServerRequest {
    //get & send clients
    private val gotClient = HttpClient{
        install(WebSockets)
    }
    private val sendClient = HttpClient{
        install(WebSockets)
    }

    //get & send queues
    private val serverQueue: ThreadSafeQueue = ThreadSafeQueue()
    private val clientQueue: ThreadSafeQueue = ThreadSafeQueue()
//    private val setterRequest: SetterRequest = SetterRequest(clientQueue)
//    private val getterRequest: GetterRequest = GetterRequest(serverQueue)

    private val lock = ReentrantLock()

    private val all_in = Thread {
        runBlocking {
            var jwt: String? = null
            val objectMapper = warped.realms.server.request.ObjectMapper()
            val client = HttpClient{
                install(WebSockets)
            }
            val auth = Auth("username", "password")
            client.webSocket (method = HttpMethod.Get, port = 8080, host = "0.0.0.0", path = "/auth") {
                send(Frame.Binary(true, objectMapper.init.writeValueAsBytes(auth)))
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    if ("[RIGHT DATA SENT]" == frame.readText()) {
                        val tokenFrame = incoming.receive() as? Frame.Text ?: throw Exception()
                        jwt = tokenFrame.readText()
                        break
                    }
                }
                this.close()
                if (jwt != null) {
                    client.webSocket (method = HttpMethod.Get, port = 8080, host = "0.0.0.0", path = "/connection") {
                        send(Frame.Text(jwt!!))
                        for (frame in incoming) {
                            frame as? Frame.Text ?: continue
                            println(frame.readText())
                        }
                    }
                }
            }
        }
    }

    //connect with server
    init {
        lock.lock()
        all_in.start()
        //get.start()
        //send.start()
    }

    //turn off sever connection
    fun dispose() {
        all_in.join()
        lock.unlock()
        //serverQueue.stackEmptyCondition.signalAll()
        //get.join()
        //clientQueue.stackFullCondition.signalAll()
        //send.join()
    }

    //hz logger?
    companion object {
        val log = logger<ServerRequest>()
    }
}
