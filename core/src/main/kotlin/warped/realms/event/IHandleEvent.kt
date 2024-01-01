package warped.realms.event

interface IHandleEvent {
    fun handle(event: Event): Boolean
}
