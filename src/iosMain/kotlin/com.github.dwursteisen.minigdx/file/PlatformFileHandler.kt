package com.github.dwursteisen.minigdx.file

import com.github.dwursteisen.minigdx.logger.Logger

actual class PlatformFileHandler(actual val logger: Logger) {
    actual fun read(filename: String): Content<String> {
        logger.error("FILE") {
            "PlatformFileHandler#read NOT IMPLEMENTED!"
        }
        val content = Content<String>(filename, logger)
        content.load("PlatformFileHandler NOT Implemented")
        return content
    }

    actual fun readData(filename: String): Content<ByteArray> {
        logger.error("FILE") {
            "PlatformFileHandler#readData NOT IMPLEMENTED!"
        }

        val content = Content<ByteArray>(filename, logger)
        content.load("PlatformFileHandler NOT Implemented".encodeToByteArray())
        return content
    }

    actual fun readTextureImage(filename: String): Content<TextureImage> {
        logger.error("FILE") {
            "PlatformFileHandler#readTextureImage NOT IMPLEMENTED!"
        }
        return Content<TextureImage>(filename, logger)
    }

    actual fun decodeTextureImage(
        filename: String,
        data: ByteArray
    ): Content<TextureImage> {
        logger.error("FILE") {
            "PlatformFileHandler#decodeTextureImage NOT IMPLEMENTED!"
        }
        return Content<TextureImage>(filename, logger)
    }

    actual fun readSound(filename: String): Content<Sound> {
        logger.error("FILE") {
            "PlatformFileHandler#readSound NOT IMPLEMENTED!"
        }
        return Content<Sound>(filename, logger)
    }
}
