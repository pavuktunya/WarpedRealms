package server_connector

import server_builder.ServerBuilder
import server_connector.request.ServerRequest

//Подключение к серверу, передача событий.
class ServerConnector() {
    private val serverRequest = ServerRequest()
    private val server_builder = ServerBuilder().also { serverRequest.serverBuilder = it }

    init {
        println("==========Server To Connect==========")
        serverRequest.startConnection()
    }

    fun push(p: ByteArray): Boolean {
        return true
    }

    fun pop(): ByteArray {
        val b = byteArrayOf(0, 0, 0, 0, 0)
        return b
    }

    fun dispose() {
        serverRequest.dispose()
        server_builder.dispose()
    }
}
