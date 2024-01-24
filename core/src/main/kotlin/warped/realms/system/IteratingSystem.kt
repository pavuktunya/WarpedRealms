package warped.realms.system

import warped.realms.world.IDispose

open class IteratingSystem(val interval: Interval = EachFrame()) : IDispose {
    var accumulator: Float = 0f
    open fun onTick(deltaTime: Float){
        when (interval) {
            is Fixed -> {
                accumulator += deltaTime
                val stepRate = interval.step

                if (accumulator < stepRate) {
                    return
                }

                while (accumulator >= stepRate) {
                    onUpdate(deltaTime)
                    accumulator -= stepRate
                }
                onAlpha(accumulator / stepRate)
            }
        }
    }

    open fun onUpdate(deltaTime: Float) {
    }
    open fun onAlpha(alpha: Float) {}
    override fun dispose() {
    }
}

open class Interval
class EachFrame : Interval()
class Fixed(val step: Float) : Interval()
