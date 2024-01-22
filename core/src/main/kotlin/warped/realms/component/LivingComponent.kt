package warped.realms.component

data class LivingComponent(
    val maxLife: Float,
    val maxShield: Float,
    var life: Float = maxLife,
    var shield: Float = maxShield
) : Component
