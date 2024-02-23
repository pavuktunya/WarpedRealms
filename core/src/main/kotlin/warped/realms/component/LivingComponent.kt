package warped.realms.component

import Component

@Component
class LivingComponent(
    val maxLife: Float,
    val maxShield: Float,
    var life: Float = maxLife,
    var shield: Float = maxShield
)
