package com.github.dwursteisen.minigdx.shaders

import com.curiouscreature.kotlin.math.Mat4
import com.github.dwursteisen.minigdx.GL
import com.github.dwursteisen.minigdx.ecs.components.Color
import com.github.dwursteisen.minigdx.math.Vector2
import com.github.dwursteisen.minigdx.math.Vector3
import kotlin.jvm.JvmName

sealed class ShaderParameter(val name: String) {

    interface ArrayParameter {

        val size: Int
    }

    interface Uniform
    interface Attribute

    interface Varying

    abstract fun create(program: ShaderProgram)

    class UniformMat4(name: String) : ShaderParameter(name), Uniform {

        override fun create(program: ShaderProgram) {
            program.createUniform(name)
        }

        fun apply(program: ShaderProgram, matrix: Mat4) {
            program.uniformMatrix4fv(program.getUniform(name), false, matrix)
        }

        override fun toString() = "uniform mat4 $name;"
    }

    class UniformArrayMat4(name: String, override val size: Int) : ShaderParameter(name), ArrayParameter, Uniform {

        override fun create(program: ShaderProgram) {
            program.createUniform(name)
        }

        @JvmName("applyArray")
        fun apply(program: ShaderProgram, matrix: Array<Mat4>) = apply(program, *matrix)

        fun apply(program: ShaderProgram, matrix: List<Mat4>) = apply(program, matrix.toTypedArray())

        fun apply(program: ShaderProgram, vararg matrix: Mat4) {
            val tmpMatrix = Array(matrix.size * 16) { 0f }

            // Copy all matrix values, aligned
            matrix.forEachIndexed { x, mat ->
                val values = mat.asGLArray()
                (0 until 16).forEach { y ->
                    tmpMatrix[x * 16 + y] = values[y]
                }
            }
            program.uniformMatrix4fv(program.getUniform(name), false, tmpMatrix)
        }

        override fun toString() = "uniform mat4 $name[$size];"
    }

    class UniformInt(name: String) : ShaderParameter(name), Uniform {

        override fun create(program: ShaderProgram) {
            program.createUniform(name)
        }

        fun apply(program: ShaderProgram, vararg value: Int) {
            when (value.size) {
                0 -> throw IllegalArgumentException("At least one int is expected")
                1 -> program.uniform1i(program.getUniform(name), value[0])
                2 -> program.uniform2i(program.getUniform(name), value[0], value[1])
                3 -> program.uniform3i(program.getUniform(name), value[0], value[1], value[2])
            }
        }

        override fun toString() = "uniform int $name;"
    }

    class UniformVec2(name: String) : ShaderParameter(name), Uniform {

        override fun create(program: ShaderProgram) {
            program.createUniform(name)
        }

        fun apply(program: ShaderProgram, vec2: Vector2) = apply(program, vec2.x, vec2.y)

        fun apply(program: ShaderProgram, vararg vec2: Float) {
            when (vec2.size) {
                2 -> program.uniform2f(program.getUniform(name), vec2[0], vec2[1])
                else -> throw IllegalArgumentException("3 values are expected. ${vec2.size} received")
            }
        }

        override fun toString() = "uniform vec2 $name;"
    }

    class UniformVec3(name: String) : ShaderParameter(name), Uniform {

        override fun create(program: ShaderProgram) {
            program.createUniform(name)
        }

        fun apply(program: ShaderProgram, vec3: Vector3) = apply(program, vec3.x, vec3.y, vec3.z)

        fun apply(program: ShaderProgram, vararg vec3: Float) {
            when (vec3.size) {
                3 -> program.uniform3f(program.getUniform(name), vec3[0], vec3[1], vec3[2])
                else -> throw IllegalArgumentException("3 values are expected. ${vec3.size} received")
            }
        }

        override fun toString() = "uniform vec3 $name;"
    }

    class UniformVec4(name: String) : ShaderParameter(name), Uniform {

        override fun create(program: ShaderProgram) {
            program.createUniform(name)
        }

        fun apply(program: ShaderProgram, color: Color) = apply(
            program,
            color.red,
            color.green,
            color.blue,
            color.alpha
        )

        fun apply(program: ShaderProgram, vararg vec4: Float) {
            when (vec4.size) {
                4 -> program.uniform4f(program.getUniform(name), vec4[0], vec4[1], vec4[2], vec4[3])
                else -> throw IllegalArgumentException("4 values are expected. ${vec4.size} received")
            }
        }

        override fun toString() = "uniform vec4 $name;"
    }

    class UniformFloat(name: String) : ShaderParameter(name), Uniform {

        override fun create(program: ShaderProgram) {
            program.createUniform(name)
        }

        fun apply(program: ShaderProgram, vararg value: Float) {
            when (value.size) {
                0 -> throw IllegalArgumentException("At least one int is expected")
                1 -> program.uniform1f(program.getUniform(name), value[0])
                2 -> program.uniform2f(program.getUniform(name), value[0], value[1])
                3 -> program.uniform3f(program.getUniform(name), value[0], value[1], value[2])
                4 -> program.uniform4f(program.getUniform(name), value[0], value[1], value[2], value[3])
            }
        }

        override fun toString(): String = "uniform float $name;"
    }

    class UniformArrayFloat(name: String, override val size: Int) : ShaderParameter(name), ArrayParameter, Uniform {

        override fun create(program: ShaderProgram) {
            program.createUniform(name)
        }

        fun apply(program: ShaderProgram, f: Array<Float>) {
            program.uniform1fv(program.getUniform(name), f)
        }

        fun apply(program: ShaderProgram, f: List<Float>) = apply(program, f.toTypedArray())

        override fun toString() = "uniform float $name[$size];"
    }

    class UniformArrayVec2(name: String, override val size: Int) : ShaderParameter(name), ArrayParameter, Uniform {

        override fun create(program: ShaderProgram) {
            program.createUniform(name)
        }

        fun apply(program: ShaderProgram, f: Array<Float>) {
            program.uniform2fv(program.getUniform(name), f)
        }

        fun apply(program: ShaderProgram, f: List<Float>) = apply(program, f.toTypedArray())

        override fun toString() = "uniform vec2 $name[$size];"
    }

    class UniformArrayVec3(name: String, override val size: Int) : ShaderParameter(name), ArrayParameter, Uniform {

        override fun create(program: ShaderProgram) {
            program.createUniform(name)
        }

        @JvmName("applyArray")
        fun apply(program: ShaderProgram, f: Array<Float>) {
            program.uniform3fv(program.getUniform(name), f)
        }

        fun apply(program: ShaderProgram, f: List<Float>) = apply(program, f.toTypedArray())

        override fun toString() = "uniform vec3 $name[$size];"
    }

    class UniformArrayVec4(name: String, override val size: Int) : ShaderParameter(name), ArrayParameter, Uniform {

        override fun create(program: ShaderProgram) {
            program.createUniform(name)
        }

        @JvmName("applyArray")
        fun apply(program: ShaderProgram, f: Array<Float>) {
            program.uniform4fv(program.getUniform(name), f)
        }

        @JvmName("applyColors")
        fun apply(program: ShaderProgram, colors: List<Color>) {
            val floats = colors.flatMap { c -> listOf(c.red, c.green, c.blue, c.alpha) }
            apply(program, floats)
        }

        fun apply(program: ShaderProgram, f: List<Float>) = apply(program, f.toTypedArray())

        override fun toString() = "uniform vec4 $name[$size];"
    }

    class AttributeVec2(name: String) : ShaderParameter(name), Attribute {

        override fun create(program: ShaderProgram) {
            program.createAttrib(name)
        }

        fun apply(program: ShaderProgram, source: Buffer) {
            program.bindBuffer(GL.ARRAY_BUFFER, source)
            program.vertexAttribPointer(
                index = program.getAttrib(name),
                size = 2,
                type = GL.FLOAT,
                normalized = false,
                stride = 0,
                offset = 0
            )
            program.enableVertexAttribArray(program.getAttrib(name))
        }

        override fun toString() = "attribute vec2 $name;"
    }

    class AttributeVec3(name: String) : ShaderParameter(name), Attribute {

        override fun create(program: ShaderProgram) {
            program.createAttrib(name)
        }

        fun apply(program: ShaderProgram, source: Buffer) {
            program.bindBuffer(GL.ARRAY_BUFFER, source)
            program.vertexAttribPointer(
                index = program.getAttrib(name),
                size = 3,
                type = GL.FLOAT,
                normalized = false,
                stride = 0,
                offset = 0
            )
            program.enableVertexAttribArray(program.getAttrib(name))
        }

        override fun toString() = "attribute vec3 $name;"
    }

    class AttributeVec4(name: String) : ShaderParameter(name), Attribute {

        override fun create(program: ShaderProgram) {
            program.createAttrib(name)
        }

        fun apply(program: ShaderProgram, source: Buffer) {
            program.bindBuffer(GL.ARRAY_BUFFER, source)
            program.vertexAttribPointer(
                index = program.getAttrib(name),
                size = 4,
                type = GL.FLOAT,
                normalized = false,
                stride = 0,
                offset = 0
            )
            program.enableVertexAttribArray(program.getAttrib(name))
        }

        override fun toString() = "attribute vec3 $name;"
    }

    class UniformSample2D(name: String) : ShaderParameter(name), Uniform {

        override fun create(program: ShaderProgram) {
            program.createUniform(name)
        }

        fun apply(program: ShaderProgram, texture: TextureReference, unit: Int = 0) {
            program.activeTexture(GL.TEXTURE0 + unit)
            program.bindTexture(GL.TEXTURE_2D, texture)
            program.uniform1i(program.getUniform(name), unit)
        }

        override fun toString() = "uniform sampler2D $name;"
    }

    class VaryingVec2(name: String) : ShaderParameter(name), Varying {

        override fun create(program: ShaderProgram) = Unit

        override fun toString() = "varying vec2 $name;"
    }

    class VaryingVec3(name: String) : ShaderParameter(name), Varying {

        override fun create(program: ShaderProgram) = Unit

        override fun toString() = "varying vec3 $name;"
    }

    class VaryingVec4(name: String) : ShaderParameter(name), Varying {

        override fun create(program: ShaderProgram) = Unit

        override fun toString() = "varying vec4 $name;"
    }

    class VaryingFloat(name: String) : ShaderParameter(name), Varying {

        override fun create(program: ShaderProgram) = Unit

        override fun toString() = "varying float $name;"
    }

    class VaryingInt(name: String) : ShaderParameter(name), Varying {

        override fun create(program: ShaderProgram) = Unit

        override fun toString() = "varying int $name;"
    }
}
