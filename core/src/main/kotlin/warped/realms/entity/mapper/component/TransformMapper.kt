package warped.realms.entity.mapper.component

import ktx.math.vec2
import warped.realms.component.MoveComponent
import warped.realms.component.TransformComponent

class TransformMapper(
    var trackerCmp: TransformComponent
) {
    val mapperCmp = TransformComponent(vec2(trackerCmp.location.x, trackerCmp.location.y))
    fun Update() {
        mapperCmp.location = vec2(trackerCmp.location.x, trackerCmp.location.y)
    }

    companion object {
        const val SIZE_ARRAY = 2
        fun TransformComponent.map(): ByteArray {
            val p = ByteArray(2)
            p[0] = this.location.x.toInt().toByte()
            p[1] = this.location.y.toInt().toByte()
            return p
        }

        fun TransformComponent.dismap(p: ByteArray) {
            this.apply {
                location.x = p[0].toFloat()
                location.y = p[1].toFloat()
            }
        }
    }
}
