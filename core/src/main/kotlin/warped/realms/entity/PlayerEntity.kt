package warped.realms.entity

import generated.systems.initEntity
import warped.realms.component.AnimationComponent
import warped.realms.component.MoveComponent
import warped.realms.component.PhysicComponent

class PlayerEntity : Entity() {
    init {
        addCmp { AnimationComponent() }
        addCmp { PhysicComponent() }
        addCmp { MoveComponent() }
        initEntity()
    }
}
