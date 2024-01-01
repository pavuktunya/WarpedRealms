package warped.realms.system

import warped.realms.event.Event

class EventSystem(
    vararg events: Event
): Event() {
    private var events :Array<Event> = arrayOf(*events)

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
