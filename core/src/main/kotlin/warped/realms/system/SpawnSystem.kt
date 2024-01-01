package warped.realms.system

import ktx.app.gdxError
import warped.realms.event.Event
import warped.realms.event.IHandleEvent
import warped.realms.event.MapChangeEvent

const val entityLayer = "entities"

class SpawnSystem: IteratingSystem(), IHandleEvent {
    override fun handle(event: Event): Boolean {
        when(event){
            is MapChangeEvent -> {
                event.mapLoader.layers.forEach { layer ->
                    if(layer.name == entityLayer){
                        layer.objects.forEach {obj->
                            val type = obj.name
                            if(type=="") gdxError("MapObject $obj does not have a type!")


                        }
                        return true;
                    }
                }
            }
        }
        return false
    }
    override fun onTick(deltaTime: Float) {
        super.onTick(deltaTime)
    }
    override fun dispose() {
        super.dispose()
    }
}
