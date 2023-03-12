package com.github.dwursteisen.minigdx

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import com.github.dwursteisen.minigdx.game.Game
import com.github.dwursteisen.minigdx.input.AndroidInputHandler

abstract class MiniGdxActivity(
    private val gameName: String = "missing game name",
    private val gameScreenConfiguration: GameScreenConfiguration,
    private val debug: Boolean = false,
    private val debugOpengGl: Boolean = false,
    private val jointLimit: Int = 50,
) : Activity() {

    private lateinit var inputHandler: AndroidInputHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        GameApplicationBuilder(
            gameConfigurationFactory = {
                GameConfiguration(
                    gameName,
                    gameScreenConfiguration,
                    debug,
                    debugOpengGl,
                    jointLimit,
                    this
                )
            },
            gameFactory = { gameContext ->
                inputHandler = gameContext.input as AndroidInputHandler
                createGame(gameContext)
            }
        ).start()
        super.onCreate(savedInstanceState)
    }

    @ExperimentalStdlibApi
    abstract fun createGame(gameContext: GameContext): Game

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        inputHandler.onKeyDown(keyCode)
        return true
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        inputHandler.onKeyUp(keyCode)
        return true
    }
}
