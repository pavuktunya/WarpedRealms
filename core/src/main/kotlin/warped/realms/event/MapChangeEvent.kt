package warped.realms.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader

class MapChangeEvent(val mapLoader: TiledMap, vararg handlers: IHandleEvent): Event(){
    private var handlers = arrayOf<IHandleEvent>(*handlers)

    fun addHandler(handler: IHandleEvent){
        handlers.plus(handler)
    }

    override fun onTick() {
        logger.debug { "Event $this get pushed" }

        super.onTick()

        handlers.forEach {
            it.handle(this)
        }
    }

    companion object{
        private val logger = ktx.log.logger<MapChangeEvent>()
    }
}
