package warped.realms.system.update

import PutComponent
import ktx.math.component1
import ktx.math.component2
import warped.realms.component.MoveComponent
import warped.realms.component.PhysicComponent
import System
import Update

@System
@Update(2)
@PutComponent(PhysicComponent::class, MoveComponent::class)
class MoveSystem {
    private val moveCmps: MutableMap<MoveComponent, PhysicComponent> = mutableMapOf()

    fun PutComponent(physCmp: PhysicComponent, moveCmp: MoveComponent) {
        moveCmps.put(moveCmp, physCmp)
        println("[DEBUG] Put component ${physCmp::class.simpleName} in ${this::class.simpleName}")
    }
    fun Update(deltaTime: Float) {
//        val x = this.javaClass.getAnnotation(Update::class.java)?.priority
//        println("[UPDATE] ${this::class.simpleName} $x")
        moveCmps.forEach { moveCmp, physCmp ->
            val mass = physCmp.body!!.mass
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
    fun DeleteComponent(component: PhysicComponent) {
        println("[DEBUG] Delete component ${component::class.simpleName} in ${this::class.simpleName}")
    }
    fun DeleteComponent(component: MoveComponent) {
        println("[DEBUG] Delete component ${component::class.simpleName} in ${this::class.simpleName}")
    }
    fun Dispose() {
        println("[DISPOSE] ${this::class.simpleName}")
    }
}
