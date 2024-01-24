package warped.realms.server

class ServerRequest {
    var flag = true

    val t1 = Thread {
        while (flag) {
            println("++ Client: Push Coord")
            Thread.sleep(1200)
            println("++ Client: Get Response")
            Thread.sleep(100)
        }
    }

    init {
        t1.start()
    }

    fun dispose() {
        flag = false
        t1.join()
    }
}
