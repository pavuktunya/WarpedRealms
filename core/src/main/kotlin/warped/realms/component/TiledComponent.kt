package warped.realms.component

import Component
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import warped.realms.entity.GameEntity

@Component
class TiledComponent {
    lateinit var cell: TiledMapTileLayer.Cell
    val nearbyEntities = mutableSetOf<TiledComponent>()
    var isActive: Boolean = false
}
