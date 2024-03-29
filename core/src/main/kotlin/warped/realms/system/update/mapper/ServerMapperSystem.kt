package warped.realms.system.update.mapper

import Update
import System
import warped.realms.component.ImageComponent
import warped.realms.entity.mapper.EntityMapper

@System
@Update(-2)
class ServerMapperSystem {
    private val entityMappers = mutableListOf<EntityMapper>()

    fun Update(deltaTime: Float) {
        entityMappers.forEach { it.Update() }
    }

    fun Dispose() {
        entityMappers.clear()
    }
}
