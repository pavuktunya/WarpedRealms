package warped.realms.system.update.mapper

import Update
import System
import server_connector.ServerConnector
import warped.realms.entity.mapper.EntityMapper

@System
@Update(12)
class ServerDismapperSystem {
    private val entityMappers = mutableListOf<EntityMapper>()
    lateinit var serverConnector: ServerConnector
    fun PutComponent(cmp: EntityMapper) {
        entityMappers.add(cmp)
    }
    fun Update(delta: Float) {
        val p = serverConnector.pop()
        entityMappers.forEach { it.dismapEntity(p) }
    }
    fun Dispose() {
        entityMappers.clear()
    }
}
