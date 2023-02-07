package com.github.dwursteisen.minigdx.file

import com.github.dwursteisen.minigdx.logger.Logger

actual class PlatformFileHandler(actual val logger: Logger) {
    actual fun read(filename: String): Content<String> {
        TODO("Not yet implemented")
    }

    actual fun readData(filename: String): Content<ByteArray> {
        TODO("Not yet implemented")
    }

    actual fun readTextureImage(filename: String): Content<TextureImage> {
        TODO("Not yet implemented")
    }

    actual fun decodeTextureImage(
        filename: String,
        data: ByteArray
    ): Content<TextureImage> {
        TODO("Not yet implemented")
    }

    actual fun readSound(filename: String): Content<Sound> {
        TODO("Not yet implemented")
    }
}
