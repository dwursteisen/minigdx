package com.github.dwursteisen.minigdx

import com.github.dwursteisen.minigdx.file.TextureImage
import com.github.dwursteisen.minigdx.shaders.Buffer
import com.github.dwursteisen.minigdx.shaders.DataSource
import com.github.dwursteisen.minigdx.shaders.FrameBufferReference
import com.github.dwursteisen.minigdx.shaders.PlatformShaderProgram
import com.github.dwursteisen.minigdx.shaders.RenderBufferReference
import com.github.dwursteisen.minigdx.shaders.Shader
import com.github.dwursteisen.minigdx.shaders.ShaderProgram
import com.github.dwursteisen.minigdx.shaders.TextureReference
import com.github.dwursteisen.minigdx.shaders.Uniform
import kotlinx.cinterop.Arena
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.COpaque
import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.cstr
import kotlinx.cinterop.get
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toCPointer
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import platform.gles3.GL_FALSE
import platform.gles3.GL_INFO_LOG_LENGTH
import platform.gles3.glActiveTexture
import platform.gles3.glAttachShader
import platform.gles3.glBindBuffer
import platform.gles3.glBindFramebuffer
import platform.gles3.glBindRenderbuffer
import platform.gles3.glBindTexture
import platform.gles3.glBlendFunc
import platform.gles3.glBufferData
import platform.gles3.glClear
import platform.gles3.glClearColor
import platform.gles3.glClearDepthf
import platform.gles3.glCompileShader
import platform.gles3.glCreateProgram
import platform.gles3.glCreateShader
import platform.gles3.glDeleteShader
import platform.gles3.glDepthFunc
import platform.gles3.glDisable
import platform.gles3.glDrawArrays
import platform.gles3.glEnable
import platform.gles3.glEnableVertexAttribArray
import platform.gles3.glFramebufferRenderbuffer
import platform.gles3.glFramebufferTexture2D
import platform.gles3.glGenBuffers
import platform.gles3.glGenFramebuffers
import platform.gles3.glGenRenderbuffers
import platform.gles3.glGenTextures
import platform.gles3.glGenerateMipmap
import platform.gles3.glGetAttribLocation
import platform.gles3.glGetProgramInfoLog
import platform.gles3.glGetProgramiv
import platform.gles3.glGetShaderInfoLog
import platform.gles3.glGetShaderiv
import platform.gles3.glGetString
import platform.gles3.glGetUniformLocation
import platform.gles3.glLinkProgram
import platform.gles3.glRenderbufferStorage
import platform.gles3.glShaderSource
import platform.gles3.glTexImage2D
import platform.gles3.glTexParameteri
import platform.gles3.glUniform1f
import platform.gles3.glUniform1fv
import platform.gles3.glUniform1i
import platform.gles3.glUniform2f
import platform.gles3.glUniform2fv
import platform.gles3.glUniform2i
import platform.gles3.glUniform3f
import platform.gles3.glUniform3fv
import platform.gles3.glUniform3i
import platform.gles3.glUniform4f
import platform.gles3.glUniform4fv
import platform.gles3.glUniformMatrix4fv
import platform.gles3.glUseProgram
import platform.gles3.glVertexAttribPointer
import platform.gles3.glViewport
import platform.glescommon.GLboolean
import platform.glescommon.GLintVar

/**
 * iOS OpenGL implementation.
 *
 * @see: https://github.com/gergelydaniel/kgl/blob/main/kgl-ios/src/nativeMain/kotlin/com.danielgergely.kgl/KglIos.kt
 * @see: https://github.com/inoutch/kotlin-gl/blob/master/src/iosArm64Main/kotlin/io/github/inoutch/kotlin/gl/api/gl.kt
 */
class OpenGL : GL {
    override fun clearColor(r: Percent, g: Percent, b: Percent, a: Percent) {
        glClearColor(r, g, b, a)
    }

    override fun clear(mask: ByteMask) {
        glClear(mask.toUInt())
    }

    override fun clearDepth(depth: Number) {
        glClearDepthf(depth.toFloat())
    }

    override fun enable(mask: ByteMask) {
        glEnable(mask.toUInt())
    }

    override fun disable(mask: ByteMask) {
        glDisable(mask.toUInt())
    }

    override fun blendFunc(sfactor: ByteMask, dfactor: ByteMask) {
        glBlendFunc(sfactor.toUInt(), dfactor.toUInt())
    }

    override fun createProgram(): ShaderProgram {
        return ShaderProgram(this, PlatformShaderProgram(glCreateProgram()))
    }

    override fun getAttribLocation(shaderProgram: ShaderProgram, name: String): Int {
        return glGetAttribLocation(shaderProgram.program.address, name)
    }

    override fun getUniformLocation(shaderProgram: ShaderProgram, name: String): Uniform {
        val address = glGetUniformLocation(shaderProgram.program.address, name)
        return Uniform(address)
    }

    override fun attachShader(shaderProgram: ShaderProgram, shader: Shader) {
        glAttachShader(shaderProgram.program.address, shader.address)
    }

    override fun linkProgram(shaderProgram: ShaderProgram) {
        glLinkProgram(shaderProgram.program.address)
    }

    override fun getProgramParameter(shaderProgram: ShaderProgram, mask: ByteMask): Any {
        val params = IntArray(1)
        params.usePinned {
            glGetProgramiv(shaderProgram.program.address, mask.toUInt(), it.addressOf(0))
        }
        return params[0]
    }

    override fun getString(parameterName: Int): String {
        val result = glGetString(parameterName.toUInt()) ?: return ""
        val array = mutableListOf<Byte>()
        var i = 0
        while (result[i] != 0u.toUByte()) {
            array.add(result[i].toByte())
            i++
        }
        return array.toString()
    }

    override fun getShaderParameter(shader: Shader, mask: ByteMask): Any {
        val params = IntArray(1)
        params.usePinned {
            glGetShaderiv(shader.address, mask.toUInt(), it.addressOf(0))
        }
        return params[0]
    }

    override fun getProgramParameterB(shaderProgram: ShaderProgram, mask: ByteMask): Boolean {
        return (getProgramParameter(shaderProgram, mask) as? Int) == 1
    }

    override fun getShaderParameterB(shader: Shader, mask: ByteMask): Boolean {
        return (getShaderParameter(shader, mask) as? Int) == 1
    }

    override fun createShader(type: ByteMask): Shader {
        return Shader(glCreateShader(type.toUInt()))
    }

    @Suppress("USELESS_CAST")
    override fun shaderSource(shader: Shader, source: String) {
        val arena = Arena()
        try {

            val input = arena.allocArray<CPointerVar<ByteVar>>(1 as Int) {
                value = source.cstr.getPointer(arena)
            }
            val inputLength = arena.allocArray<GLintVar>(1 as Int) {
                value = source.length
            }

            glShaderSource(shader.address, 1, input, inputLength)
        } finally {
            arena.clear()
        }
    }

    override fun compileShader(shader: Shader) {
        glCompileShader(shader.address)
    }

    override fun getShaderInfoLog(shader: Shader): String {
        val infoLogLength = getShaderParameter(shader, GL_INFO_LOG_LENGTH) as Int
        val infoLog = ByteArray(infoLogLength + 1)
        infoLog.usePinned {
            glGetShaderInfoLog(shader.address, infoLogLength + 1, null, it.addressOf(0))
        }
        return infoLog.decodeToString()
    }

    override fun deleteShader(shader: Shader) {
        glDeleteShader(shader.address)
    }

    override fun getProgramInfoLog(shader: ShaderProgram): String {
        val infoLogLength = getProgramParameter(shader, GL_INFO_LOG_LENGTH) as Int
        val infoLog = ByteArray(infoLogLength + 1) // +1 for null terminator
        infoLog.usePinned {
            glGetProgramInfoLog(shader.program.address, infoLogLength + 1, null, it.addressOf(0))
        }
        return infoLog.decodeToString()
    }

    override fun createBuffer(): Buffer {
        val buffers = UIntArray(1)
        buffers.usePinned {
            glGenBuffers(1, it.addressOf(0))
        }
        return Buffer(buffers[0])
    }

    override fun bindBuffer(target: ByteMask, buffer: Buffer) {
        glBindBuffer(target.toUInt(), buffer.address)
    }

    override fun bufferData(target: ByteMask, data: DataSource, usage: Int) {
        val size = when (data) {
            is DataSource.FloatDataSource -> data.floats.size
            is DataSource.IntDataSource -> data.ints.size
            is DataSource.ShortDataSource -> data.shorts.size
            is DataSource.DoubleDataSource -> data.double.size
            is DataSource.UIntDataSource -> data.ints.size
        }

        when (data) {
            is DataSource.FloatDataSource -> data.floats.usePinned {
                glBufferData(target.toUInt(), size.toLong(), it.addressOf(0), usage.toUInt())
            }

            is DataSource.IntDataSource -> data.ints.usePinned {
                glBufferData(target.toUInt(), size.toLong(), it.addressOf(0), usage.toUInt())
            }

            is DataSource.ShortDataSource -> data.shorts.usePinned {
                glBufferData(target.toUInt(), size.toLong(), it.addressOf(0), usage.toUInt())
            }

            is DataSource.UIntDataSource -> data.ints.usePinned {
                glBufferData(target.toUInt(), size.toLong(), it.addressOf(0), usage.toUInt())
            }

            is DataSource.DoubleDataSource -> data.double.usePinned {
                glBufferData(target.toUInt(), size.toLong(), it.addressOf(0), usage.toUInt())
            }
        }
    }

    override fun texParameteri(target: Int, paramName: Int, paramValue: Int) {
        glTexParameteri(target.toUInt(), paramName.toUInt(), paramValue)
    }

    override fun depthFunc(target: ByteMask) {
        glDepthFunc(target.toUInt())
    }

    override fun vertexAttribPointer(index: Int, size: Int, type: Int, normalized: Boolean, stride: Int, offset: Int) {
        glVertexAttribPointer(
            index.toUInt(),
            size,
            type.toUInt(),
            normalized.toGl(),
            stride,
            offset.toLong().toCPointer<COpaque>()
        )
    }

    override fun enableVertexAttribArray(index: Int) {
        glEnableVertexAttribArray(index.toUInt())
    }

    override fun useProgram(shaderProgram: ShaderProgram) {
        glUseProgram(shaderProgram.program.address)
    }

    override fun createTexture(): TextureReference {
        val textures = UIntArray(1)
        textures.usePinned {
            glGenTextures(1, it.addressOf(0))
        }
        return TextureReference(textures[0])
    }

    override fun createFrameBuffer(): FrameBufferReference {
        val buffer = UIntArray(1)
        buffer.usePinned {
            glGenFramebuffers(1, it.addressOf(0))
        }
        return FrameBufferReference(buffer[0])
    }

    override fun bindFrameBuffer(frameBufferReference: FrameBufferReference) {
        glBindFramebuffer(GL.FRAMEBUFFER.toUInt(), frameBufferReference.address)
    }

    override fun frameBufferTexture2D(attachmentPoint: Int, textureReference: TextureReference, level: Int) {
        glFramebufferTexture2D(
            GL.FRAMEBUFFER.toUInt(),
            attachmentPoint.toUInt(),
            GL.TEXTURE_2D.toUInt(),
            textureReference.address,
            level
        )
    }

    override fun bindDefaultFrameBuffer() {
        glBindFramebuffer(GL.FRAMEBUFFER.toUInt(), 0u)
    }

    override fun createRenderBuffer(): RenderBufferReference {
        val buffer = UIntArray(1)
        buffer.usePinned {
            glGenRenderbuffers(1, it.addressOf(0))
            return RenderBufferReference(buffer[0])
        }
    }

    override fun bindRenderBuffer(renderBufferReference: RenderBufferReference) {
        glBindRenderbuffer(GL.RENDERBUFFER.toUInt(), renderBufferReference.address)
    }

    override fun renderBufferStorage(internalformat: Int, width: Int, height: Int) {
        glRenderbufferStorage(GL.RENDERBUFFER.toUInt(), internalformat.toUInt(), width, height)
    }

    override fun framebufferRenderbuffer(attachementType: Int, renderBufferReference: RenderBufferReference) {
        glFramebufferRenderbuffer(
            GL.FRAMEBUFFER.toUInt(),
            attachementType.toUInt(),
            GL.RENDERBUFFER.toUInt(),
            renderBufferReference.address,
        )
    }

    override fun activeTexture(byteMask: ByteMask) {
        glActiveTexture(byteMask.toUInt())
    }

    override fun bindTexture(target: Int, textureReference: TextureReference) {
        glBindTexture(target.toUInt(), textureReference.address)
    }

    override fun uniformMatrix4fv(uniform: Uniform, transpose: Boolean, data: Array<Float>) {
        glUniformMatrix4fv(uniform.address, 1, transpose.toGl(), data.toFloatArray().refTo(0))
    }

    override fun uniform1i(uniform: Uniform, data: Int) {
        glUniform1i(uniform.address, data)
    }

    override fun uniform2i(uniform: Uniform, a: Int, b: Int) {
        glUniform2i(uniform.address, a, b)
    }

    override fun uniform3i(uniform: Uniform, a: Int, b: Int, c: Int) {
        glUniform3i(uniform.address, a, b, c)
    }

    override fun uniform1f(uniform: Uniform, first: Float) {
        glUniform1f(uniform.address, first)
    }

    override fun uniform2f(uniform: Uniform, first: Float, second: Float) {
        glUniform2f(uniform.address, first, second)
    }

    override fun uniform3f(uniform: Uniform, first: Float, second: Float, third: Float) {
        glUniform3f(uniform.address, first, second, third)
    }

    override fun uniform4f(uniform: Uniform, first: Float, second: Float, third: Float, fourth: Float) {
        glUniform4f(uniform.address, first, second, third, fourth)
    }

    override fun uniform1fv(uniform: Uniform, floats: Array<Float>) {
        glUniform1fv(uniform.address, floats.size, floats.toFloatArray().refTo(0))
    }

    override fun uniform2fv(uniform: Uniform, floats: Array<Float>) {
        glUniform2fv(uniform.address, floats.size, floats.toFloatArray().refTo(0))
    }

    override fun uniform3fv(uniform: Uniform, floats: Array<Float>) {
        glUniform3fv(uniform.address, floats.size, floats.toFloatArray().refTo(0))
    }

    override fun uniform4fv(uniform: Uniform, floats: Array<Float>) {
        glUniform4fv(uniform.address, floats.size, floats.toFloatArray().refTo(0))
    }

    override fun drawArrays(mask: ByteMask, offset: Int, vertexCount: Int) {
        glDrawArrays(mask.toUInt(), offset, vertexCount)
    }

    override fun drawElements(mask: ByteMask, vertexCount: Int, type: Int, offset: Int) {
        // The interface needs to be updated to pass the indices.
        // On other implementation, it can be skip. Don't think it will be possible here.
        // glDrawElements(mask.toUInt(), vertexCount, type.toUInt(), offset)
        TODO()
    }

    override fun viewport(x: Int, y: Int, width: Int, height: Int) {
        glViewport(x, y, width, height)
    }

    override fun texImage2D(
        target: Int,
        level: Int,
        internalformat: Int,
        format: Int,
        type: Int,
        source: TextureImage
    ) {
        texImage2D(
            target,
            level,
            internalformat,
            format,
            source.width,
            source.height,
            type,
            source.source
        )
    }

    override fun texImage2D(
        target: Int,
        level: Int,
        internalformat: Int,
        format: Int,
        width: Int,
        height: Int,
        type: Int,
        source: ByteArray
    ) {
        glTexImage2D(
            target.toUInt(),
            level,
            internalformat,
            width,
            height,
            0,
            format.toUInt(),
            type.toUInt(),
            source.refTo(0)
        )
    }

    override fun generateMipmap(target: Int) {
        glGenerateMipmap(target.toUInt())
    }

    private fun Boolean.toGl(): GLboolean = if (this) 1u else 0u

    private fun UByte.toBoolean() = this != GL_FALSE.toUByte()
}
