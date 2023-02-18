package com.github.dwursteisen.minigdx.logger

class IOSLogger : Logger {

    private fun printMessage(
        level: Logger.LogLevel,
        tag: String,
        exception: Throwable?,
        message: () -> String
    ) {
        if (rootLevel.ordinal <= level.ordinal) {
            println("[${level.name}]\t($tag) - ${message()}")
            exception?.printStackTrace()
        }
    }

    override fun debug(tag: String, message: () -> String) {
        printMessage(Logger.LogLevel.DEBUG, tag, null, message)
    }

    override fun debug(tag: String, exception: Throwable, message: () -> String) {
        printMessage(Logger.LogLevel.DEBUG, tag, exception, message)
    }

    override fun info(tag: String, message: () -> String) {
        printMessage(Logger.LogLevel.DEBUG, tag, null, message)
    }

    override fun info(tag: String, exception: Throwable, message: () -> String) {
        printMessage(Logger.LogLevel.INFO, tag, exception, message)
    }

    override fun warn(tag: String, message: () -> String) {
        printMessage(Logger.LogLevel.WARN, tag, null, message)
    }

    override fun warn(tag: String, exception: Throwable, message: () -> String) {
        printMessage(Logger.LogLevel.WARN, tag, exception, message)
    }

    override fun error(tag: String, message: () -> String) {
        printMessage(Logger.LogLevel.ERROR, tag, null, message)
    }

    override fun error(tag: String, exception: Throwable, message: () -> String) {
        printMessage(Logger.LogLevel.ERROR, tag, exception, message)
    }

    override var rootLevel: Logger.LogLevel = Logger.LogLevel.DEBUG
}
