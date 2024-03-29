package server_connector

import server_builder.ServerBuilder
import server_connector.request.ServerRequest

//Подключение к серверу, передача событий.
class ServerConnector() {
    val serverRequest = ServerRequest()
    val server_builder = ServerBuilder()

    init {
        println("==========Server To Connect==========")
    }

    fun push(p: ByteArray): Boolean {
        return true
    }

    fun pop(): ByteArray {
        val b = byteArrayOf(0, 0, 0, 0, 0)
        return b
    }

    fun Dispose() {
        serverRequest.dispose()
        server_builder.dispose()
    }
}
