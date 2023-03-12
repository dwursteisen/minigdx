package com.github.dwursteisen.minigdx.graphics

import com.curiouscreature.kotlin.math.Mat4
import com.github.dwursteisen.minigdx.ByteMask
import com.github.dwursteisen.minigdx.GL
import com.github.dwursteisen.minigdx.Percent
import com.github.dwursteisen.minigdx.Pixel
import com.github.dwursteisen.minigdx.file.TextureImage
import com.github.dwursteisen.minigdx.logger.Logger
import com.github.dwursteisen.minigdx.shaders.Buffer
import com.github.dwursteisen.minigdx.shaders.DataSource
import com.github.dwursteisen.minigdx.shaders.FrameBufferReference
import com.github.dwursteisen.minigdx.shaders.RenderBufferReference
import com.github.dwursteisen.minigdx.shaders.Shader
import com.github.dwursteisen.minigdx.shaders.ShaderProgram
import com.github.dwursteisen.minigdx.shaders.TextureReference
import com.github.dwursteisen.minigdx.shaders.Uniform

class LogGL(val logger: Logger, val delegate: GL) : GL {

    private var callNumber: Long = 0

    private fun <T> log(methodName: String, action: () -> T): T {
        logger.info("GL") {
            "---> [$callNumber] \t$methodName"
        }
        val result = action()
        logger.info("GL") {
            "<--- [$callNumber] \t$methodName"
        }
        callNumber++
        return result
    }

    override fun clearColor(r: Percent, g: Percent, b: Percent, a: Percent) = log("clearColor") {
        delegate.clearColor(r, g, b, a)
    }

    override fun clear(mask: ByteMask) = log("clear") {
        delegate.clear(mask)
    }

    override fun clearDepth(depth: Number) = log("clearDepth") {
        delegate.clearDepth(depth)
    }

    override fun enable(mask: ByteMask) = log("enable") {
        delegate.enable(mask)
    }

    override fun disable(mask: ByteMask) = log("disable") {
        delegate.disable(mask)
    }

    override fun blendFunc(sfactor: ByteMask, dfactor: ByteMask) = log("blendFunc") {
        delegate.blendFunc(sfactor, dfactor)
    }

    override fun createProgram(): ShaderProgram = log("createProgram") {
        delegate.createProgram()
    }

    override fun getAttribLocation(shaderProgram: ShaderProgram, name: String): Int =
        log("getAttribLocation") {
            delegate.getAttribLocation(shaderProgram, name)
        }

    override fun getUniformLocation(shaderProgram: ShaderProgram, name: String): Uniform =
        log("getUniformLocation") {
            delegate.getUniformLocation(shaderProgram, name)
        }

    override fun attachShader(shaderProgram: ShaderProgram, shader: Shader) = log("attachShader") {
        delegate.attachShader(shaderProgram, shader)
    }

    override fun linkProgram(shaderProgram: ShaderProgram) = log("lingProgram") {
        delegate.linkProgram(shaderProgram)
    }

    override fun getString(parameterName: Int): String? = log("getString") {
        delegate.getString(parameterName)
    }

    override fun getProgramParameter(shaderProgram: ShaderProgram, mask: ByteMask): Any =
        log("getProgramParameter") {
            delegate.getProgramParameter(shaderProgram, mask)
        }

    override fun getShaderParameter(shader: Shader, mask: ByteMask): Any =
        log("getShaderParameter") {
            delegate.getShaderParameter(shader, mask)
        }

    override fun getShaderParameterB(shader: Shader, mask: ByteMask): Boolean = log("getShaderParameterB") {
         delegate.getShaderParameterB(shader, mask)
    }

    override fun getProgramParameterB(shaderProgram: ShaderProgram, mask: ByteMask): Boolean = log("getProgramParameterB"){
        delegate.getProgramParameterB(shaderProgram, mask)
    }

    override fun createShader(type: ByteMask): Shader = log("createShader") {
        delegate.createShader(type)
    }

    override fun shaderSource(shader: Shader, source: String) = log("shaderSource") {
        delegate.shaderSource(shader, source)
    }

    override fun compileShader(shader: Shader) = log("compileShader") {
        delegate.compileShader(shader)
    }

    override fun getShaderInfoLog(shader: Shader): String = log("getShaderInfoLog") {
        delegate.getShaderInfoLog(shader)
    }

    override fun deleteShader(shader: Shader) = log("deleteShader") {
        delegate.deleteShader(shader)
    }

    override fun getProgramInfoLog(shader: ShaderProgram): String = log("getProgramInfoLog") {
        delegate.getProgramInfoLog(shader)
    }

    override fun createBuffer(): Buffer = log("createBuffer") {
        delegate.createBuffer()
    }

    override fun createFrameBuffer(): FrameBufferReference = log("createFrameBuffer") {
        delegate.createFrameBuffer()
    }

    override fun bindFrameBuffer(frameBufferReference: FrameBufferReference) =
        log("bindFrameBuffer") {
            delegate.bindFrameBuffer(frameBufferReference)
        }

    override fun bindDefaultFrameBuffer() = log("bindDefaultFrameBuffer") {
        delegate.bindDefaultFrameBuffer()
    }

    override fun createRenderBuffer(): RenderBufferReference = log("createRenderBuffer") {
        delegate.createRenderBuffer()
    }

    override fun bindRenderBuffer(renderBufferReference: RenderBufferReference) =
        log("bindRenderBuffer") {
            delegate.bindRenderBuffer(renderBufferReference)
        }

    override fun renderBufferStorage(internalformat: Int, width: Int, height: Int) =
        log("renderBufferStorage") {
            delegate.renderBufferStorage(internalformat, width, height)
        }

    override fun framebufferRenderbuffer(
        attachementType: Int,
        renderBufferReference: RenderBufferReference
    ) = log("framebufferRenderbuffer") {
        delegate.framebufferRenderbuffer(attachementType, renderBufferReference)
    }

    override fun frameBufferTexture2D(
        attachmentPoint: Int,
        textureReference: TextureReference,
        level: Int
    ) = log("frameBufferTexture2D") {
        delegate.frameBufferTexture2D(attachmentPoint, textureReference, level)
    }

    override fun bindBuffer(target: ByteMask, buffer: Buffer) = log("bindBuffer") {
        delegate.bindBuffer(target, buffer)
    }

    override fun bufferData(target: ByteMask, data: DataSource, usage: Int) = log("bufferData") {
        delegate.bufferData(target, data, usage)
    }

    override fun depthFunc(target: ByteMask) = log("depthFunc") {
        delegate.depthFunc(target)
    }

    override fun vertexAttribPointer(
        index: Int,
        size: Int,
        type: Int,
        normalized: Boolean,
        stride: Int,
        offset: Int
    ) = log("vertexAttribPointer"){
        delegate.vertexAttribPointer(index, size, type, normalized, stride, offset)
    }

    override fun enableVertexAttribArray(index: Int) = log("enableVertexAttribArray") {
        delegate.enableVertexAttribArray(index)
    }

    override fun useProgram(shaderProgram: ShaderProgram) = log("useProgram") {
        delegate.useProgram(shaderProgram)
    }

    override fun createTexture(): TextureReference = log("createTexture") {
        delegate.createTexture()
    }

    override fun activeTexture(byteMask: ByteMask) = log("activeTexture") {
        delegate.activeTexture(byteMask)
    }

    override fun bindTexture(target: Int, textureReference: TextureReference) = log("bindTexture") {
        delegate.bindTexture(target, textureReference)
    }

    override fun uniformMatrix4fv(uniform: Uniform, transpose: Boolean, data: Array<Float>) =
        log("uniformMatrix4fv") {
            delegate.uniformMatrix4fv(uniform, transpose, data)
        }

    override fun uniform1i(uniform: Uniform, data: Int) = log("uniform1i") {
        delegate.uniform1i(uniform, data)
    }

    override fun uniform2i(uniform: Uniform, a: Int, b: Int) = log("uniform2i") {
        delegate.uniform2i(uniform, a, b)
    }

    override fun uniform3i(uniform: Uniform, a: Int, b: Int, c: Int) = log("uniform3i") {
        delegate.uniform3i(uniform, a, b, c)
    }

    override fun uniform1f(uniform: Uniform, first: Float) = log("uniform1f") {
        delegate.uniform1f(uniform, first)
    }

    override fun uniform2f(uniform: Uniform, first: Float, second: Float) = log("uniform2f") {
        delegate.uniform2f(uniform, first, second)
    }

    override fun uniform3f(uniform: Uniform, first: Float, second: Float, third: Float) =
        log("uniform3f") {
            delegate.uniform3f(uniform, first, second, third)
        }

    override fun uniform4f(
        uniform: Uniform,
        first: Float,
        second: Float,
        third: Float,
        fourth: Float
    ) = log("uniform4f"){
        delegate.uniform4f(uniform, first, second, third, fourth)
    }

    override fun uniform1fv(uniform: Uniform, floats: Array<Float>) = log("uniform1fv") {
        delegate.uniform1fv(uniform, floats)
    }

    override fun uniform2fv(uniform: Uniform, floats: Array<Float>) = log("uniform2fv") {
        delegate.uniform2fv(uniform, floats)
    }

    override fun uniform3fv(uniform: Uniform, floats: Array<Float>) = log("uniform3fv") {
        delegate.uniform3fv(uniform, floats)
    }

    override fun uniform4fv(uniform: Uniform, floats: Array<Float>) = log("uniform4fv") {
        delegate.uniform4fv(uniform, floats)
    }

    override fun uniformMatrix4fv(uniform: Uniform, transpose: Boolean, data: Mat4) = log("uniformMatrix4fv") {
        delegate.uniformMatrix4fv(uniform, transpose, data)
    }

    override fun uniformMatrix4fv(uniform: Uniform, transpose: Boolean, data: FloatArray) = log("uniformMatrix4fv") {
        delegate.uniformMatrix4fv(uniform, transpose, data)
    }

    override fun drawArrays(mask: ByteMask, offset: Int, vertexCount: Int) = log("drawArrays") {
        delegate.drawArrays(mask, offset, vertexCount)
    }

    override fun drawElements(mask: ByteMask, vertexCount: Int, type: Int, offset: Int) =
        log("drawElements") {
            delegate.drawElements(mask, vertexCount, type, offset)
        }

    override fun viewport(x: Pixel, y: Pixel, width: Pixel, height: Pixel) = log("viewport") {
        delegate.viewport(x, y, width, height)
    }

    override fun texImage2D(
        target: Int,
        level: Int,
        internalformat: Int,
        format: Int,
        type: Int,
        source: TextureImage
    ) {
        delegate.texImage2D(target, level, internalformat, format, type, source)
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
        delegate.texImage2D(target, level, internalformat, format, width, height, type, source)
    }

    override fun texParameteri(target: Int, paramName: Int, paramValue: Int) =
        log("texParameteri") {
            delegate.texParameteri(target, paramName, paramValue)
        }

    override fun generateMipmap(target: Int) = log("generateMipmap") {
        delegate.generateMipmap(target)
    }
}