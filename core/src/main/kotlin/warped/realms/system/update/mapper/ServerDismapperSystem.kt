package warped.realms.system.update.mapper

import Update
import System
import server_connector.ServerConnector
import warped.realms.entity.mapper.EntityMapper

@System
@Update(100)
class ServerDismapperSystem {
    private val entityMappers = mutableListOf<EntityMapper>()
    lateinit var serverConnector: ServerConnector
    fun Update(delta: Float) {
        val p = arrayOf(serverConnector.serverRequest.serverQueue.pop().toByte())
        entityMappers.forEach { it.dismapEntity(p.toByteArray()) }
    }

    fun Dispose() {
        entityMappers.clear()
    }
}
