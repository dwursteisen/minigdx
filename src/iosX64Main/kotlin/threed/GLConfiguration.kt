package threed

import threed.file.FileHandler
import threed.input.InputHandler

actual class GLConfiguration

actual class GLContext actual constructor(configuration: GLConfiguration) {
    internal actual fun createContext(): GL {
        TODO("Not yet implemented")
    }

    internal actual fun createFileHandler(): FileHandler {
        TODO("Not yet implemented")
    }

    internal actual fun createInputHandler(): InputHandler {
        TODO("Not yet implemented")
    }

    actual fun run(gameFactory: () -> Game) {
    }
}
