package com.github.dwursteisen.minigdx

import com.github.dwursteisen.minigdx.file.FileHandler
import com.github.dwursteisen.minigdx.game.Game
import com.github.dwursteisen.minigdx.graphics.FillViewportStrategy
import com.github.dwursteisen.minigdx.graphics.ViewportStrategy
import com.github.dwursteisen.minigdx.input.InputHandler
import com.github.dwursteisen.minigdx.logger.Logger

actual open class PlatformContextCommon actual constructor(actual override val configuration: GameConfiguration) :
    PlatformContext {

    override var postRenderLoop: () -> Unit = {}

    actual override fun createGL(): GL = TODO()

    actual override fun createFileHandler(logger: Logger, gameContext: GameContext): FileHandler {
        TODO()
    }

    actual override fun createInputHandler(logger: Logger, gameContext: GameContext): InputHandler {
        TODO()
    }

    actual override fun createViewportStrategy(logger: Logger): ViewportStrategy {
        return FillViewportStrategy(logger)
    }

    actual override fun createLogger(): Logger {
        TODO()
    }

    actual override fun createOptions(): Options {
        return Options(configuration.debug, configuration.jointLimit)
    }

    actual override fun start(gameFactory: (GameContext) -> Game) {
        TODO()
    }
}
