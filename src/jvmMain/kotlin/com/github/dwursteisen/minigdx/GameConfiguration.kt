package com.github.dwursteisen.minigdx

actual class GameConfiguration(
    actual val gameName: String,
    actual val gameScreenConfiguration: GameScreenConfiguration,
    actual val debug: Boolean = false,
    /**
     * Is OpenGL should log all commands?
     */
    actual val debugOpenGl: Boolean = false,
    actual val jointLimit: Int = 50,
    /**
     * Configuration of the application window.
     */
    val window: Window,
    /**
     * Run the game using wireframe as display mode
     * instead of solid polygons.
     */
    val wireframe: Boolean = false
)
