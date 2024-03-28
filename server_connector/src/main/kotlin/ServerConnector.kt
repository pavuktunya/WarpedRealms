package server_connector

import server_builder.ServerBuilder
import server_connector.request.ServerRequest

//Подключение к серверу, передача событий.
class ServerConnector() {
    val serverRequest = ServerRequest()
    val server_builder = ServerBuilder()

    init {
        println("Server Connector")
    }
    fun dispose() {
        serverRequest.dispose()
        server_builder.dispose()
    }
}
