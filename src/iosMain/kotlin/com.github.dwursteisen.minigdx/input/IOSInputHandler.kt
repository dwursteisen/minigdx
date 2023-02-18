package com.github.dwursteisen.minigdx.input

import com.github.dwursteisen.minigdx.logger.Logger
import com.github.dwursteisen.minigdx.math.ImmutableVector2
import com.github.dwursteisen.minigdx.math.Vector2

class IOSInputHandler(private val logger: Logger) : InputHandler {
    override fun isKeyJustPressed(key: Key): Boolean {
        logger.error("INPUT") {
            "IOSInputHandler#isKeyJustPressed NOT IMPLEMENTED!"
        }
        return false
    }

    override fun isKeyPressed(key: Key): Boolean {
        logger.error("INPUT") {
            "IOSInputHandler#isKeyPressed NOT IMPLEMENTED!"
        }
        return false
    }

    override fun isTouched(signal: TouchSignal): Vector2? {
        logger.error("INPUT") {
            "IOSInputHandler#isTouched NOT IMPLEMENTED!"
        }
        return null
    }

    override fun isJustTouched(signal: TouchSignal): Vector2? {
        logger.error("INPUT") {
            "IOSInputHandler#isJustTouched NOT IMPLEMENTED!"
        }
        return null
    }

    override val currentTouch: ImmutableVector2 = ImmutableVector2(Vector2(0f, 0f))
}
