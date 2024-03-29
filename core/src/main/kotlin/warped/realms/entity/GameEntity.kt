package warped.realms.entity

import generated.systems.initEntity
import warped.realms.component.*

class GameEntity(
    imCmp: ImageComponent.() -> ImageComponent,
    anCmp: AnimationComponent.() -> AnimationComponent,
    trCmp: TransformComponent.() -> TransformComponent,
    phCmp: PhysicComponent.() -> PhysicComponent,
    mvCmp: MoveComponent.() -> MoveComponent,
    tileCmp: (TiledComponent.() -> TiledComponent)?
) : Entity() {
    init {
        addCmp<AnimationComponent>(anCmp)
        addCmp<ImageComponent>(imCmp)
        addCmp<MoveComponent>(mvCmp)
        addCmp<PhysicComponent>(phCmp)
        addCmp<TransformComponent>(trCmp)
        if (tileCmp != null) {
            addCmp<TiledComponent>(tileCmp)
        }
        initEntity()
    }

    fun addCollisionComponent(lambda: TiledComponent.() -> TiledComponent) {
        addCmp<TiledComponent>(lambda)
        initEntity()
    }
}
