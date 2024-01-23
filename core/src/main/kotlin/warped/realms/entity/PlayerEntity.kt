package warped.realms.entity

import warped.realms.component.*
import warped.realms.world.*

class PlayerEntity(
    liveEntity: LiveEntity,
    physicComponent: PhysicComponent = liveEntity.physicComponent,
    moveComponent: MoveComponent = liveEntity.moveComponent
) : LiveEntity(
    moveComponent,
    physicComponent,
    liveEntity.entityComponent
), ILivingComponent {
    override val livingComponent = LivingComponent(100f, 100f)
}
