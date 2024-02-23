package warped.realms.system

import ktx.log.logger

object Logger {
    val logger = logger<Logger>()
}

fun Logger.debug(p: () -> String) {
    Logger.logger.debug(p)
}

fun Logger.error(p: () -> String) {
    Logger.logger.error(p)
}
