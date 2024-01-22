package warped.realms.system

import ktx.math.component1
import ktx.math.component2
import warped.realms.component.MoveComponent
import warped.realms.component.PhysicComponent

class MoveSystem(
    private val moveCmps: MutableMap<MoveComponent, PhysicComponent> = mutableMapOf()
) : IteratingSystem() {
    fun addMoveComponent(vararg _moveCmps: Pair<MoveComponent, PhysicComponent>) {
        moveCmps.putAll(mutableMapOf(*_moveCmps))
    }

    override fun onTick(deltaTime: Float) {
        super.onTick(deltaTime)
        moveCmps.forEach { moveCmp, physCmp ->
            val mass = physCmp.body.mass
            val (velX, velY) = physCmp.body.linearVelocity
            if (moveCmp.cos == 0f && moveCmp.sin == 0f) {
                physCmp.impulse.set(
                    mass * (0f - velX),
                    mass * (0f - velY)
                )
            }
            physCmp.impulse.set(
                mass * (moveCmp.speed * moveCmp.cos - velX),
                mass * (moveCmp.speed * moveCmp.sin - velY)
            )
        }
    }

    override fun dispose() {
        super.dispose()
    }
}
