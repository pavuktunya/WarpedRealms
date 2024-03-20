package warped.realms.entity

import generated.systems.initEntity
import warped.realms.component.*

class GameEntity(
    imCmp: () -> ImageComponent,
    anCmp: () -> AnimationComponent,
    trCmp: () -> TransformComponent,
    phCmp: () -> PhysicComponent,
    mvCmp: () -> MoveComponent,
    tileCmp: (() -> TiledComponent)?
) : Entity() {
    init {
        addCmp<AnimationComponent> { anCmp.invoke() }
        addCmp<ImageComponent> { imCmp.invoke() }
        addCmp<MoveComponent> { mvCmp.invoke() }
        addCmp<PhysicComponent> { phCmp.invoke() }
        addCmp<TransformComponent> { trCmp.invoke() }
        if (tileCmp != null) {
            addCmp<TiledComponent> { tileCmp.invoke() }
        }
        initEntity()
    }

    fun addCollisionComponent(lambda: () -> TiledComponent) {
        addCmp<TiledComponent> { lambda.invoke() }
        initEntity()
    }
}
