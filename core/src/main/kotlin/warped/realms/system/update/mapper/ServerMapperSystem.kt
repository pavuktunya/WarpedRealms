package warped.realms.system.update.mapper

import Update
import System
import server_connector.ServerConnector
import warped.realms.component.ImageComponent
import warped.realms.entity.mapper.EntityMapper

@System
class ServerMapperSystem {
    private val entityMappers = mutableListOf<EntityMapper>()
    lateinit var serverConnector: ServerConnector

    fun Update(deltaTime: Float) {
        entityMappers.forEach { it.Update() }
        serverConnector.push(entityMappers.push())
    }

    fun PutComponent(cmp: EntityMapper) {
        entityMappers.add(cmp)
    }

    fun Dispose() {
        entityMappers.clear()
    }
    fun MutableList<EntityMapper>.push(): ByteArray {
        val b = byteArrayOf(0, 0, 0, 0, 0)
        return b
    }
}
