package warped.realms.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.app.gdxError
import ktx.collections.map
import ktx.log.logger
import warped.realms.component.AnimationComponent
import warped.realms.component.AnimationComponent.Companion.NO_ANIMATION
import warped.realms.component.ImageComponent

class AnimationSystem(
    private val textureAtlas: TextureAtlas,
    //private val animationCmp: AnimationComponent,
    vararg animCmps: Pair<AnimationComponent, ImageComponent>
):IteratingSystem() {
    private val animCmps: MutableMap<AnimationComponent, ImageComponent> = mutableMapOf(*animCmps)
    private val cachedAnimations = mutableMapOf<String, Animation<TextureRegionDrawable>>()
    override fun onTick(deltaTime: Float) {
        super.onTick(deltaTime)
        animCmps.forEach { animationCmp, imageCmp ->
            if (animationCmp.nextAnimation == NO_ANIMATION) {
                animationCmp.stateTime += deltaTime
            } else {
                animationCmp.animation = animation(animationCmp.nextAnimation)

                animationCmp.stateTime = 0f
                animationCmp.nextAnimation = NO_ANIMATION
            }
            animationCmp.animation.playMode = animationCmp.playMode
            imageCmp.image.drawable = animationCmp.animation.getKeyFrame(animationCmp.stateTime)
        }
    }

    fun addAnimationComponent(vararg _animCmps: Pair<AnimationComponent, ImageComponent>) {
        animCmps.putAll(mutableMapOf(*_animCmps))
    }

    private fun animation(aniKeyPath: String): Animation<TextureRegionDrawable>{
        return cachedAnimations.getOrPut(aniKeyPath){
            log.debug { "New animation is created for '$aniKeyPath'" }

            val regions = textureAtlas.findRegions(aniKeyPath)
            if(regions.isEmpty){
                gdxError("There are no texture regions for $aniKeyPath")
            }

            Animation<TextureRegionDrawable>(
                DEFAULT_FRAME_DURATION,
                regions.map { TextureRegionDrawable(it) }
            )
        }
    }

    override fun dispose() {
        super.dispose()
    }

    companion object{
        private val log = logger<AnimationSystem>()
        private const val DEFAULT_FRAME_DURATION = 1/8f
    }
}
