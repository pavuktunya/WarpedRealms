package warped.realms.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import Component

@Component
class AnimationComponent(
    var atlasKey: AnimationModel = AnimationModel.UNDEFINED,
    var stateTime:Float=0f,
    var playMode: PlayMode = Animation.PlayMode.LOOP,
    var nextAnimation:String = ""
){
    lateinit var animation: Animation<TextureRegionDrawable>

    fun nextAnimation(atlasKey: AnimationModel, type: AnimationType){
        this.atlasKey=atlasKey
        nextAnimation="${atlasKey.atlasKey}/${type.atlasKey}"
    }
    companion object{
        val NO_ANIMATION = ""
    }
}
enum class AnimationModel{
    FANTAZY_WARRIOR, RAT, CHEST, UNDEFINED;
    val atlasKey:String = this. toString().lowercase()
}
enum class AnimationType {
    IDLE, RUN, ATTACK, DEATH, OPEN;
    val atlasKey:String = this.toString().lowercase()
}
