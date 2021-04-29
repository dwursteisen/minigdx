package com.github.dwursteisen.minigdx.ecs.components

import com.dwursteisen.minigdx.scene.api.model.Normal
import com.dwursteisen.minigdx.scene.api.model.UV
import com.dwursteisen.minigdx.scene.api.model.Vertex
import com.github.dwursteisen.minigdx.Pixel
import com.github.dwursteisen.minigdx.Seconds
import com.github.dwursteisen.minigdx.ecs.components.gl.BoundingBox
import com.github.dwursteisen.minigdx.ecs.components.gl.MeshPrimitive
import com.github.dwursteisen.minigdx.ecs.components.text.TextEffect
import com.github.dwursteisen.minigdx.ecs.components.text.WriteText
import com.github.dwursteisen.minigdx.ecs.entities.Entity
import com.github.dwursteisen.minigdx.file.Font
import kotlin.reflect.KClass

sealed class HorizontalAlignment {

    abstract fun getHorizontalOffset(lineWith: Pixel, scale: Float, min: Float, max: Float): Float
    object Left : HorizontalAlignment() {

        override fun getHorizontalOffset(lineWith: Pixel, scale: Float, min: Float, max: Float): Float = min
    }

    object Right : HorizontalAlignment() {

        override fun getHorizontalOffset(lineWith: Pixel, scale: Float, min: Float, max: Float): Float {
            return max - lineWith * scale
        }
    }

    object Center : HorizontalAlignment() {

        override fun getHorizontalOffset(lineWith: Pixel, scale: Float, min: Float, max: Float): Float {
            return (max - min) * 0.5f + min - lineWith * scale * 0.5f
        }
    }
}

sealed class VerticalAlignment {
    object Top : VerticalAlignment()
    object Center : VerticalAlignment()
    object Bottom : VerticalAlignment()
}

typealias NumberOfCharacter = Int

internal class TextMeshData(
    val vertices: MutableList<Vertex> = mutableListOf(),
    val verticesOrder: MutableList<Int> = mutableListOf()
)

class TextComponent(
    text: TextEffect,
    font: Font,
    lineWith: NumberOfCharacter? = null,
    horizontalAlign: HorizontalAlignment = HorizontalAlignment.Left,
    verticalAlign: VerticalAlignment = VerticalAlignment.Top,
) : Component {

    private var needsToBeUpdated: Boolean = true

    private var _text: TextEffect = text
    private var _font: Font = font
    private var _lineWith: NumberOfCharacter = lineWith ?: findLongestLine(text.content).length
    private var _horizontalAlign: HorizontalAlignment = horizontalAlign
    private var _verticalAlign: VerticalAlignment = verticalAlign

    var text: TextEffect
        get() {
            if (needsToBeUpdated) update()
            return _text
        }
        set(value) {
            needsToBeUpdated = true
            _text = value
        }

    var font: Font
        get() {
            if (needsToBeUpdated) update()
            return _font
        }
        set(value) {
            needsToBeUpdated = true
            _font = value
        }

    var lineWith: NumberOfCharacter
        get() {
            if (needsToBeUpdated) update()
            return _lineWith
        }
        set(value) {
            needsToBeUpdated = true
            _lineWith = value
        }

    var horizontalAlign: HorizontalAlignment
        get() {
            if (needsToBeUpdated) update()
            return _horizontalAlign
        }
        set(value) {
            needsToBeUpdated = true
            _horizontalAlign = value
        }

    var verticalAlign: VerticalAlignment
        get() {
            if (needsToBeUpdated) update()
            return _verticalAlign
        }
        set(value) {
            needsToBeUpdated = true
            _verticalAlign = value
        }

    var owner: Entity? = null

    constructor(
        text: String,
        font: Font,
        lineWith: NumberOfCharacter? = null,
        horizontalAlign: HorizontalAlignment = HorizontalAlignment.Left,
        verticalAlign: VerticalAlignment = VerticalAlignment.Top
    ) : this(WriteText(text), font, lineWith, horizontalAlign, verticalAlign)

    private val textMeshData = TextMeshData()

    override fun onAdded(entity: Entity) {
        owner = entity
        update()
    }

    override fun onRemoved(entity: Entity) {
        owner = null
    }

    override fun onComponentUpdated(componentType: KClass<out Component>) {
        if (componentType == BoundingBox::class) {
            needsToBeUpdated = true
        }
    }

    fun update(delta: Seconds) {
        if (needsToBeUpdated) update()

        _text.update(delta)
        if (_text.wasUpdated) {
            update()
        }
    }

    private fun update() {
        val longestLine = findLongestLine(_text.content)
        val scale = computeCharacterScale(longestLine)
        val textMeshData = generateVertices(scale)
        val meshPrimitive = owner!!.get(MeshPrimitive::class)
        meshPrimitive.isUVDirty = true
        meshPrimitive.isDirty = true
        meshPrimitive.primitive.vertices = textMeshData.vertices
        meshPrimitive.primitive.verticesOrder = textMeshData.verticesOrder.toIntArray()
        needsToBeUpdated = false
    }

    /**
     * Generate the vertices map regarding the current text.
     *
     * The text will fit in the box by keeping [lineWidth] as reference.
     */
    internal fun generateVertices(scale: Float): TextMeshData {
        val boundingBox = owner!!.get(BoundingBox::class)
        val textureHeight = _font.fontSprite.height.toFloat()
        val textureWidth = _font.fontSprite.width.toFloat()

        textMeshData.vertices.clear()
        textMeshData.verticesOrder.clear()

        val angelCode = _font.angelCode
        var characterIndex = 0

        val lines = _text.content.split("\n")
        var lineIndex = 0
        // Compute the start x for the horizontal aligment
        var startX = _horizontalAlign.getHorizontalOffset(
            getTextSize(lines[lineIndex], _font),
            scale,
            boundingBox.min.x,
            boundingBox.max.x
        )

        // HALIGN: From top
        val startY = boundingBox.localMax.y

        var currentX = 0f
        var currentY = 0f
        _text.content.forEach { char ->
            // Advance cursor
            if (char == '\n') {
                // next line
                lineIndex++

                // Recompute the start x as regarding the vertical alignment, it can be something else.
                startX = _horizontalAlign.getHorizontalOffset(
                    getTextSize(lines[lineIndex], _font),
                    scale,
                    boundingBox.min.x,
                    boundingBox.max.x
                )

                currentX = 0f
                currentY -= angelCode.info.lineHeight
            } else if (char == ' ') {
                currentX += angelCode.characters[char]?.width ?: angelCode.characters['a']?.width ?: 0
            }

            val code = angelCode.characters[char] ?: return@forEach
            val yTop = currentY - code.yoffset
            val xLeft = currentX + code.xoffset
            val xRight = xLeft + code.width
            val yBottom = yTop - code.height

            // up left
            val a = Vertex(
                com.dwursteisen.minigdx.scene.api.model.Position(
                    startX + scale * xLeft,
                    startY + scale * yTop,
                    0f
                ),
                DEFAULT_NORMAL,
                uv = UV(
                    code.x / textureWidth,
                    code.y / textureHeight
                )
            )
            val b = Vertex(
                com.dwursteisen.minigdx.scene.api.model.Position(
                    startX + scale * xLeft,
                    startY + scale * yBottom,
                    0f
                ),
                DEFAULT_NORMAL,
                uv = UV(
                    code.x / textureWidth,
                    (code.y + code.height) / textureHeight
                )
            )

            val c = Vertex(
                com.dwursteisen.minigdx.scene.api.model.Position(
                    startX + scale * xRight,
                    startY + scale * yBottom,
                    0f
                ),
                DEFAULT_NORMAL,
                uv = UV(
                    (code.x + code.width) / textureWidth,
                    (code.y + code.height) / textureHeight
                )
            )

            val d = Vertex(
                com.dwursteisen.minigdx.scene.api.model.Position(
                    startX + scale * xRight,
                    startY + scale * yTop,
                    0f
                ),
                Normal(0f, 0f, 0f),
                uv = UV(
                    (code.x + code.width) / textureWidth,
                    code.y / textureHeight
                )
            )

            textMeshData.vertices.addAll(listOf(a, b, c, d))
            textMeshData.verticesOrder.addAll(
                listOf(
                    characterIndex * 4 + 0,
                    characterIndex * 4 + 1,
                    characterIndex * 4 + 2,
                    characterIndex * 4 + 0,
                    characterIndex * 4 + 2,
                    characterIndex * 4 + 3
                )
            )

            currentX += code.xadvance + code.xoffset
            characterIndex++
        }
        return textMeshData
    }

    internal fun computeCharacterScale(longestLine: String): Float {
        val boundingBox = owner!!.get(BoundingBox::class)

        // Look for the longest line and deduce the character size in worl unit from it.
        val numberOfCharacterByLine: NumberOfCharacter = _lineWith
        val lineInPixel: Pixel = getTextSize(longestLine, _font)
        if (longestLine.isEmpty()) {
            return 0f
        }
        val characterWidth: Float = (lineInPixel / longestLine.length).toFloat()
        val lineInWorldUnit = boundingBox.localMax.x - boundingBox.localMin.x
        // Characters size will be scaled to match the size in world unit.
        val scale = lineInWorldUnit / (numberOfCharacterByLine * characterWidth)
        return scale
    }

    /**
     * Find the longest line in the text.
     */
    private fun findLongestLine(content: String): String {
        return content.split("\n").maxByOrNull { it.length } ?: content
    }

    /**
     * Compute the length of the [text] in pixel using the [font].
     *
     * The text should be only one line otherwise the length will be cumulative.
     */
    private fun getTextSize(text: String, font: Font): Pixel {
        return text.map { char ->
            val angelCharacter = font[char]
            var xAdvance = angelCharacter.xadvance
            xAdvance += angelCharacter.xoffset
            xAdvance
        }.sum()
    }

    companion object {

        private val DEFAULT_NORMAL = Normal(0f, 0f, 1f)
    }
}
