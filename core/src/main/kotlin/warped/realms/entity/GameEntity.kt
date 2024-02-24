package warped.realms.entity

import generated.systems.initEntity
import warped.realms.component.*

class GameEntity(
    imCmp: () -> ImageComponent,
    anCmp: () -> AnimationComponent,
    trCmp: () -> TransformComponent,
    phCmp: () -> PhysicComponent,
    mvCmp: () -> MoveComponent
) : Entity() {
    init {
        addCmp<AnimationComponent> { anCmp.invoke() }
        addCmp<ImageComponent> { imCmp.invoke() }
        addCmp<MoveComponent> { mvCmp.invoke() }
        addCmp<PhysicComponent> { phCmp.invoke() }
        addCmp<TransformComponent> { trCmp.invoke() }

        initEntity()
    }
}
