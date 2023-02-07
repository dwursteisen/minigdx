import com.github.dwursteisen.minigdx.GameConfiguration
import com.github.dwursteisen.minigdx.GameScreenConfiguration
import com.github.dwursteisen.minigdx.file.TextureImage

actual fun createGameConfiguration(): GameConfiguration {
    return GameConfiguration(
        GameScreenConfiguration.WithCurrentScreenResolution(),
        "game name",
        false,
        50,
    )
}

actual fun createTextureImage(): TextureImage {
    return TextureImage(10, 10)
}
