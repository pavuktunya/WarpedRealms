package server_connector

//Подключение к серверу, передача событий.
class ServerConnector() {
    val serverRequest = ServerRequest()

    fun dispose() {
        serverRequest.dispose()
    }
}
