package com.github.dwursteisen.minigdx

import com.github.dwursteisen.minigdx.game.Game
import kotlinx.cinterop.useContents
import platform.EAGL.EAGLContext
import platform.GLKit.GLKView
import platform.GLKit.GLKViewController
import platform.UIKit.UIScreen

enum class EnumEAGLRenderingAPI(val iosValue: ULong) {


    OpenGLES1(1u),

    OpenGLES2(2u),

    OpenGLES3(3u)
}

enum class EnumGLKViewDrawableDepthFormat(val iosValue: Int) {


    FormatNone(0),
    Format16(1),
    Format24(2),
}

abstract class MinigdxDelegate(val gameFactory: (GameContext) -> Game) {

    fun viewDidLoad(controller: GLKViewController) {
        val context = EAGLContext(EnumEAGLRenderingAPI.OpenGLES3.iosValue)
        EAGLContext.setCurrentContext(context)

        val view = controller.view as GLKView
        view.setContext(context)
        view.drawableDepthFormat = EnumGLKViewDrawableDepthFormat.Format24.iosValue

        startGame()
    }

    fun getMainScreenSize() : Resolution {
        return UIScreen.mainScreen.bounds().useContents {
            Resolution(this.size.width.toInt(), this.size.height.toInt())
        }
    }

    fun startGame() {

        GameApplicationBuilder(
            gameConfigurationFactory = {
                GameConfiguration(
                    gameScreenConfiguration = GameScreenConfiguration.WithRatio(9f / 16f),
                    gameName = "IOS Game",
                    debug = true,
                    debugOpenGl = true,
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
