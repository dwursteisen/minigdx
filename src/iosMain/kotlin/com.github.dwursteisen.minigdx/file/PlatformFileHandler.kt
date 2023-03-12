package com.github.dwursteisen.minigdx.file

import cnames.structs.CGImage
import com.github.dwursteisen.minigdx.logger.Logger
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGContextDrawImage
import platform.CoreGraphics.CGContextRelease
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.create
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIImage

actual class PlatformFileHandler(actual val logger: Logger) {
    actual fun read(filename: String): Content<String> {
        return readData(filename).map { it.decodeToString() }
    }

    actual fun readData(filename: String): Content<ByteArray> {
        val index = filename.indexOfLast { it == '.' }
        val (name, extension) = if (index == -1) {
            filename to ""
        } else {
            filename.take(index) to filename.substring(index).drop(1)
        }
        val url =
            NSBundle.mainBundle().URLForResource(name, extension) ?: throw IllegalArgumentException(
                "Filename '$filename' (named '$name' with extension '$extension') wasn't found " + "in the main bundle. Did you include this file in your iOS project (in XCode) ?"
            )
        val data = NSData.dataWithContentsOfURL(url)!!

        val bytes = data.bytes()?.readBytes(data.length.toInt())
        val content = Content<ByteArray>(filename, logger)
        content.load(bytes)
        return content
    }

    actual fun readTextureImage(filename: String): Content<TextureImage> {
        return readData(filename).flatMap { bytes -> decodeTextureImage(filename, bytes) }
    }

    actual fun decodeTextureImage(
        filename: String, data: ByteArray
    ): Content<TextureImage> {
        val imageData = memScoped {
            NSData.create(bytes = allocArrayOf(data), length = data.size.toULong())
        }

        val image = UIImage.imageWithData(imageData) ?: throw IllegalStateException(
            "imageWithData return null. Is your file exist and is a supported image format?"
        )
        val (width, height) = image.size.useContents { (this.width * image.scale) to (this.height * image.scale) }

        val uncompressedData = extractRGBA(image, width.toInt(), height.toInt())

        val content = Content<TextureImage>(filename, logger)
        content.load(TextureImage(width.toInt(), height.toInt(), uncompressedData))

        return content
    }

    private fun extractRGBA(image: UIImage, width: Int, height: Int): ByteArray {
        // Extract the RGBA raw content
        val cgImage: CPointer<CGImage> = image.CGImage ?:
                                        throw IllegalArgumentException("Failed to extract CGImage from image")

        val bytesPerPixel = 4 // RGBA -> 4 bits
        val bytesPerRow = bytesPerPixel * width

        // https://github.com/inoutch/kotchan/blob/master/src/iosMain/kotlin/io/github/inoutch/kotchan/utility/graphic/Image.kt
        return memScoped {
            val imageData = allocArray<ByteVar>(height * bytesPerRow)

            val colorSpace = CGColorSpaceCreateDeviceRGB()
            val bitmapInfo = CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value

            val context = CGBitmapContextCreate(
                imageData,
                width.toULong(),
                height.toULong(),
                8,
                bytesPerRow.toULong(),
                colorSpace,
                bitmapInfo
            )

            val rect = CGRectMake(0.0, 0.0, width.toDouble(), height.toDouble())

            // Render the image into imageData
            CGContextDrawImage(context, rect, cgImage)
            CGContextRelease(context)

            imageData.readBytes(bytesPerRow * height)
        }
    }

    actual fun readSound(filename: String): Content<Sound> {
        logger.error("FILE") {
            "PlatformFileHandler#readSound NOT IMPLEMENTED!"
        }
        return Content<Sound>(filename, logger)
    }
}
