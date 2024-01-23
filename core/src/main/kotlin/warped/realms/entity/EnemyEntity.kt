package warped.realms.entity

import warped.realms.component.*

class EnemyEntity(
    liveEntity: LiveEntity,
    physicComponent: PhysicComponent = liveEntity.physicComponent,
    moveComponent: MoveComponent = liveEntity.moveComponent
) : LiveEntity(
    moveComponent,
    physicComponent,
    liveEntity.entityComponent
) {

}
