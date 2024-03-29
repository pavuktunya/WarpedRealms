package warped.realms.factory

import Factory
import warped.realms.component.TiledComponent

@Factory(TiledComponent::class)
class TiledFactory {
    fun Factory(lambda: TiledComponent.() -> TiledComponent): TiledComponent {
        return TiledComponent().lambda()
    }

    fun Delete(cmp: TiledComponent) {

    }
}
