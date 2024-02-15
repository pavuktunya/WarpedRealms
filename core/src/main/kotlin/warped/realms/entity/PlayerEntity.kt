package warped.realms.entity

import warped.realms.component.*

class PlayerEntity(
    liveEntity: LiveEntity,
    physicComponent: PhysicComponent = liveEntity.physicComponent,
    moveComponent: MoveComponent = liveEntity.moveComponent,
    val livingComponent: LivingComponent = LivingComponent(100f, 100f)
) : LiveEntity(
    moveComponent,
    physicComponent,
    liveEntity.entityComponent
)
