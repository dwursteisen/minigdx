package com.github.dwursteisen.minigdx.file

import com.github.dwursteisen.minigdx.entity.primitives.Texture
import com.github.dwursteisen.minigdx.entity.text.AngelCode
import com.github.dwursteisen.minigdx.entity.text.Font

class FontLoader : FileLoader<Font> {

    @ExperimentalStdlibApi
    override fun load(filename: String, handler: FileHandler): Content<Font> {
        val angelCode: Content<AngelCode> = handler.get("$filename.fnt")
        val textureContent: Content<Texture> = handler.get("$filename.png")

        return textureContent.flatMap { texture ->
            angelCode.map {
                Font(
                    angelCode = it,
                    fontSprite = texture
                )
            }
        }
    }
}
