package com.github.dwursteisen.minigdx.internal

import android.content.Context
import android.opengl.GLSurfaceView
import com.github.dwursteisen.minigdx.Game
import com.github.dwursteisen.minigdx.GameContext
import com.github.dwursteisen.minigdx.MiniGdxActivity
import com.github.dwursteisen.minigdx.input.InputManager
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

@ExperimentalStdlibApi
class MiniGdxSurfaceView(private val gameContext: GameContext, context: Context) : GLSurfaceView(context) {

    init {
        setEGLContextClientVersion(3)

        setRenderer(object : Renderer {

            private var time = 0f

            lateinit var game: Game
            lateinit var inputManager: InputManager

            override fun onDrawFrame(gl: GL10?) {
                val now = System.nanoTime().toFloat()
                val delta = (now - time) / 1000000000.0f

                inputManager.record()
                game.render(delta)
                inputManager.reset()
                time = now
            }

            override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
                gameContext.viewport.update(gameContext.gl, game.worldResolution, width, height)
            }

            @ExperimentalStdlibApi
            override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
                inputManager = gameContext.input as InputManager
                game = (context as MiniGdxActivity).createGame(gameContext)
                game.create()
            }
        })
        // Render the view only when there is a change in the drawing data
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        setOnTouchListener(gameContext.input as OnTouchListener)
    }
}
