package warped.realms.event

import com.badlogic.gdx.maps.tiled.TiledMap
import warped.realms.system.Logger
import warped.realms.system.debug

class MapChangeEvent(val mapLoader: TiledMap, vararg handlers: IHandleEvent) : Event {
    private var handlers = arrayOf<IHandleEvent>(*handlers)

    fun addHandler(handler: IHandleEvent){
        handlers.plus(handler)
    }

    override fun onTick() {
        Logger.debug { "Event $this get pushed" }
        handlers.forEach {
            it.handle(this)
        }
    }
}
