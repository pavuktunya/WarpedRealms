package warped.realms.system.update

import PutComponent
import ktx.math.component1
import ktx.math.component2
import warped.realms.component.MoveComponent
import warped.realms.component.PhysicComponent
import System
import Update

@System
@Update(1)
@PutComponent(MoveComponent::class, PhysicComponent::class)
class MoveSystem {
    private val moveCmps: MutableMap<MoveComponent, PhysicComponent> = mutableMapOf()
    fun addMoveComponent(vararg _moveCmps: Pair<MoveComponent, PhysicComponent>) {
        moveCmps.putAll(mutableMapOf(*_moveCmps))
    }

    fun Update(deltaTime: Float) {
        val x = this.javaClass.getAnnotation(Update::class.java)?.priority
        println("[UPDATE] ${this::class.simpleName} $x")

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

    fun PutComponent(component: PhysicComponent) {

        println("[DEBUG] Put component ${component::class.simpleName} in ${this::class.simpleName}")
    }

    fun DeleteComponent(component: PhysicComponent) {

        println("[DEBUG] Delete component ${component::class.simpleName} in ${this::class.simpleName}")
    }

    fun PutComponent(component: MoveComponent) {

        println("[DEBUG] Put component ${component::class.simpleName} in ${this::class.simpleName}")
    }

    fun DeleteComponent(component: MoveComponent) {

        println("[DEBUG] Delete component ${component::class.simpleName} in ${this::class.simpleName}")
    }

    fun Dispose() {
        println("[DISPOSE] ${this::class.simpleName}")
    }
}
