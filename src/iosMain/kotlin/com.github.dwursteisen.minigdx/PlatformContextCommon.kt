package com.github.dwursteisen.minigdx

import com.github.dwursteisen.minigdx.file.FileHandler
import com.github.dwursteisen.minigdx.file.FileHandlerCommon
import com.github.dwursteisen.minigdx.file.PlatformFileHandler
import com.github.dwursteisen.minigdx.game.Game
import com.github.dwursteisen.minigdx.game.GameWrapper
import com.github.dwursteisen.minigdx.graphics.FillViewportStrategy
import com.github.dwursteisen.minigdx.graphics.ViewportStrategy
import com.github.dwursteisen.minigdx.input.IOSInputHandler
import com.github.dwursteisen.minigdx.input.InputHandler
import com.github.dwursteisen.minigdx.logger.IOSLogger
import com.github.dwursteisen.minigdx.logger.Logger

actual open class PlatformContextCommon actual constructor(
    actual override val configuration: GameConfiguration
) : PlatformContext {

    override var postRenderLoop: () -> Unit = {}

    private lateinit var gameWrapper: GameWrapper

    actual override fun createGL(): GL = OpenGL()

    actual override fun createFileHandler(logger: Logger, gameContext: GameContext): FileHandler {
        return FileHandlerCommon(
            gameContext = gameContext,
            logger = logger,
            platformFileHandler = PlatformFileHandler(logger)
        )
    }

    actual override fun createInputHandler(logger: Logger, gameContext: GameContext): InputHandler {
        return IOSInputHandler(logger)
    }

    actual override fun createViewportStrategy(logger: Logger): ViewportStrategy {
        return FillViewportStrategy(logger)
    }

    actual override fun createLogger(): Logger {
        return IOSLogger()
    }

    actual override fun createOptions(): Options {
        return Options(configuration.debug, configuration.jointLimit)
    }

    actual override fun start(gameFactory: (GameContext) -> Game) {
        // Compute the Game Resolution regarding the configuration
        val (width, height) = configuration.minigdxDelegate.getMainScreenSize()

        val gameResolution = configuration.gameScreenConfiguration.screen(
            width,
            height,
        )

        val gameContext = GameContext(this, gameResolution)
        gameContext.logPlatform()

        gameContext.deviceScreen.height = height
        gameContext.deviceScreen.width = width
        gameContext.frameBufferScreen.height = width
        gameContext.frameBufferScreen.width = height
        gameContext.viewport.update(
            gameContext.gl,
            gameResolution,
            gameResolution
        )

        this.gameWrapper = GameWrapper(gameContext, gameFactory(gameContext))

        gameWrapper.create()
        gameWrapper.resume()

        configuration.minigdxDelegate.registerUpdateMethod(::update)
    }

    fun update(delta: Seconds) {
        gameWrapper.render(delta)
        postRenderLoop()
        postRenderLoop = { }
    }
}
