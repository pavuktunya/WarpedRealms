package warped.realms.event

import warped.realms.system.Logger
import warped.realms.system.debug

interface Event {
    var handlers: Array<IHandleEvent>
    fun onTick() {
        Logger.debug { "Event $this get pushed" }
        handlers.forEach {
            it.handle(this)
        }
    }

    fun addHandler(handler: IHandleEvent) {
        handlers.plus(handler)
    }
}
