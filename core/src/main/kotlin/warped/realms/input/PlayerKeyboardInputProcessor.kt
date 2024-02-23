package warped.realms.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
import ktx.app.KtxInputAdapter
import ktx.log.logger
import warped.realms.component.MoveComponent
import System

@System
class PlayerKeyboardInputProcessor : KtxInputAdapter {
    private val moveCmps: MutableList<MoveComponent> = mutableListOf()

    private var playerCos: Float = 0f
    private var playerSin: Float = 0f
    fun addMoveCmp(moveComponent: MoveComponent) {
        moveCmps.add(moveComponent)
    }

    init {
        Gdx.input.inputProcessor = this
        //Gdx.input.inputProcessor = InputMultiplexer()
    }

    fun Dispose() {

    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode.isMovementKey()) {
            when (keycode) {
                UP -> playerSin = 1f
                DOWN -> playerSin = -1f
                LEFT -> playerCos = -1f
                RIGHT -> playerCos = 1f
            }
            updatePlayerMovement()
            return true
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode.isMovementKey()) {
            when (keycode) {
                UP -> playerSin = if (Gdx.input.isKeyPressed(DOWN)) -1f else 0f
                DOWN -> playerSin = if (Gdx.input.isKeyPressed(UP)) 1f else 0f
                LEFT -> playerCos = if (Gdx.input.isKeyPressed(RIGHT)) 1f else 0f
                RIGHT -> playerCos = if (Gdx.input.isKeyPressed(LEFT)) -1f else 0f
            }
            updatePlayerMovement()
            return true
        }
        return false
    }

    private fun updatePlayerMovement() {
        moveCmps.forEach {
            it.apply {
                cos = playerCos
                sin = playerSin
            }
        }
    }

    private fun Int.isMovementKey(): Boolean {
        return this == UP || this == DOWN || this == RIGHT || this == LEFT
    }

    companion object {
        val logger = logger<PlayerKeyboardInputProcessor>()
    }
}
