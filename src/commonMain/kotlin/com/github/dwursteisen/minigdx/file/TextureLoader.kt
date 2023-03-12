package com.github.dwursteisen.minigdx.file

import com.dwursteisen.minigdx.scene.api.common.Id

class TextureLoader : FileLoader<Texture> {

    private val textureImageLoader = TextureImageLoader()

    override fun load(filename: String, handler: FileHandler): Content<Texture> {
        val result = Texture(
            Id(),
            byteArrayOf(
                0xFF.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFF.toByte(),
                0x00.toByte(), 0xFF.toByte(), 0x00.toByte(), 0xFF.toByte(),
                0x00.toByte(), 0x00.toByte(), 0xFF.toByte(), 0xFF.toByte(),
                0xFF.toByte(), 0xFF.toByte(), 0x00.toByte(), 0xFF.toByte()
            ),
            2,
            2,
            hasAlpha = false
        )

        // Load the default texture
        handler.gameContext.assetsManager.add(result)

        val content = handler.create(filename, result)

        textureImageLoader.load(filename, handler).onLoaded { textureImage ->
            handler.gameContext.logger.warn("TEXTURE"){ "TEXTURE IMAGE LOADED ${textureImage.width} ${textureImage.height}"}
            // The final texture is loaded.
            // Load this texture instead of the default one.
            //result.textureImage = textureImage
            //result.height = textureImage.height
            //result.width = textureImage.width
            // FIXME: We don't care about the texture as drawing with this texture make the application crash

            // handler.gameContext.assetsManager.add(result)
        }
        return content
    }
}
