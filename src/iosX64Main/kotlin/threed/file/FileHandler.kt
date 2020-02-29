package threed.file

actual class FileHandler {
    actual fun read(fileName: String): Content<String> {
        TODO("Not yet implemented")
    }

    actual fun readData(filename: String): Content<ByteArray> {
        TODO("Not yet implemented")
    }

    actual val isLoaded: Boolean
        get() = TODO("Not yet implemented")
    actual val loadProgression: Float
        get() = TODO("Not yet implemented")
}
