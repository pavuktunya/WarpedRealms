package server_logic

import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.math.vec2
import server_logic.mapper.GamePackage
import server_logic.mapper.ServerPackage

// Игровая логика: game Loop и обработка событий, физика

class ServerGameLogic {
    private val phWorld = createWorld(gravity = vec2()).apply {
        setAutoClearForces(false)
    }

    fun onTick(delta: Float): GamePackage {
        println("[SERVER]=====================")
        return GamePackage()
    }

    fun updatePackage(serverPackage: ServerPackage) {

    }

    fun dispose() {
        phWorld.disposeSafely()
    }
}
