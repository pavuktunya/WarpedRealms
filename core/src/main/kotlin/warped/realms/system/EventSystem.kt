package warped.realms.system

import warped.realms.event.Event
import System

@System
class EventSystem : Event() {
    private val events: Array<Event> = arrayOf()
    fun Dispose() {

    }
    fun addEvent(event: Event){
        events.plus(event)
    }
    override fun onTick() {
        super.onTick()
        events.forEach {
            it.onTick()
        }
    }
}
