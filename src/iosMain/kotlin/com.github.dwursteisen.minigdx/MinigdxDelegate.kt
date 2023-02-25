package com.github.dwursteisen.minigdx

import com.github.dwursteisen.minigdx.game.Game
import platform.CoreGraphics.CGFloat

abstract class MinigdxDelegate(val gameFactory: (GameContext) -> Game) {

    var width: Float = 0f
    var height: Float = 0f

    fun startGame(width: CGFloat, height: CGFloat) {
        this.width = width.toFloat()
        this.height = height.toFloat()

        GameApplicationBuilder(
            gameConfigurationFactory = {
                GameConfiguration(
                    gameScreenConfiguration = GameScreenConfiguration.WithRatio(9f / 16f),
                    gameName = "IOS Game",
                    debug = true,
                    minigdxDelegate = this
                )
            },
            gameFactory = gameFactory,
        ).start()
    }

    fun onTouch() {
        println("onTouch - from Kotlin")
    }

    private var onUpdateDelegate: (Seconds) -> Unit = { }

    fun onUpdate(delta: Seconds) {
        println("onUpdate $delta - from Kotlin")
        onUpdateDelegate(delta)
    }

    internal fun registerUpdateMethod(update: (Seconds) -> Unit) {
        onUpdateDelegate = update
    }
}
