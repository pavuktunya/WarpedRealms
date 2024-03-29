package warped.realms.entity.mapper.component

import warped.realms.component.MoveComponent

class MoveMapper(
    var trackerCmp: MoveComponent
) {
    val mapperCmp = MoveComponent(
        trackerCmp.speed,
        trackerCmp.cos,
        trackerCmp.sin
    )

    fun Update() {
        mapperCmp.speed = trackerCmp.speed
        mapperCmp.cos = trackerCmp.cos
        mapperCmp.sin = trackerCmp.sin
    }

    companion object {
        const val SIZE_ARRAY = 3
        fun MoveComponent.map(): ByteArray {
            val p = ByteArray(3)
            p[0] = this.speed.toInt().toByte()
            p[1] = this.cos.toInt().toByte()
            p[2] = this.sin.toInt().toByte()
            return p
        }

        fun MoveComponent.dismap(p: ByteArray) {
            this.apply {
                this.speed = p[0].toFloat()
                this.cos = p[1].toFloat()
                this.sin = p[2].toFloat()
            }
        }
    }
}
