package warped.realms.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import warped.realms.system.Logger
import warped.realms.system.debug

class CollisionDespawnEvent(
    vararg handlers: IHandleEvent
) : Event {
    lateinit var cell: Cell
    override var handlers: Array<IHandleEvent> = arrayOf(*handlers)
}
