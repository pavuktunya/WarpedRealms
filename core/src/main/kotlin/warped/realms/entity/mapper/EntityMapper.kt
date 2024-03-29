package warped.realms.entity.mapper

import warped.realms.component.MoveComponent
import warped.realms.component.TransformComponent
import warped.realms.entity.GameEntity
import warped.realms.entity.mapper.component.MoveMapper
import warped.realms.entity.mapper.component.MoveMapper.Companion.dismap
import warped.realms.entity.mapper.component.MoveMapper.Companion.map
import warped.realms.entity.mapper.component.TransformMapper
import warped.realms.entity.mapper.component.TransformMapper.Companion.dismap
import warped.realms.entity.mapper.component.TransformMapper.Companion.map

class EntityMapper(
    var entity: GameEntity
) {
    val moveMapper = MoveMapper(entity.getCmp<MoveComponent>())
    val transMapper = TransformMapper(entity.getCmp<TransformComponent>())
    fun Update() {
        moveMapper.Update()
        transMapper.Update()
    }

    fun mapEntity(): ByteArray {
        val p1 = moveMapper.mapperCmp.map()
        val p2 = transMapper.mapperCmp.map()
        return p1 + p2
    }

    fun dismapEntity(p: ByteArray) {
        moveMapper.mapperCmp.dismap(p.copyOfRange(0, MoveMapper.SIZE_ARRAY))
        transMapper.mapperCmp.dismap(
            p.copyOfRange(
                MoveMapper.SIZE_ARRAY + 1,
                TransformMapper.SIZE_ARRAY + MoveMapper.SIZE_ARRAY
            )
        )
    }
}
