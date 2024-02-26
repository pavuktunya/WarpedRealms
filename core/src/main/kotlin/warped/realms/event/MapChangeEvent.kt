package warped.realms.event

import com.badlogic.gdx.maps.tiled.TiledMap
import warped.realms.system.Logger
import warped.realms.system.debug

class MapChangeEvent(val mapLoader: TiledMap, vararg handlers: IHandleEvent) : Event {
    override var handlers = arrayOf<IHandleEvent>(*handlers)
}
