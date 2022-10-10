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
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint16Array
import org.khronos.webgl.Uint32Array
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.WebGLRenderingContextBase

class WebGL(private val gl: WebGLRenderingContextBase) : GL {

    override fun clearColor(r: Percent, g: Percent, b: Percent, a: Percent) {
        gl.clearColor(r.toPercent(), g.toPercent(), b.toPercent(), a.toPercent())
    }

    override fun clear(mask: ByteMask) {
        gl.clear(mask)
    }

    override fun clearDepth(depth: Number) {
        gl.clearDepth(depth.toFloat())
    }

    override fun enable(mask: ByteMask) {
        gl.enable(mask)
    }

    override fun disable(mask: ByteMask) {
        gl.disable(mask)
    }

    override fun blendFunc(sfactor: ByteMask, dfactor: ByteMask) {
        gl.blendFunc(sfactor, dfactor)
    }

    override fun depthFunc(target: ByteMask) {
        gl.depthFunc(target)
    }

    override fun vertexAttribPointer(index: Int, size: Int, type: Int, normalized: Boolean, stride: Int, offset: Int) {
        gl.vertexAttribPointer(index, size, type, normalized, stride, offset)
    }

    override fun createProgram(): ShaderProgram {
        return ShaderProgram(this, PlatformShaderProgram(gl.createProgram()!!))
    }

    override fun useProgram(shaderProgram: ShaderProgram) {
        gl.useProgram(shaderProgram.program.delegate)
    }

    override fun getAttribLocation(shaderProgram: ShaderProgram, name: String): Int {
        return gl.getAttribLocation(shaderProgram.program.delegate, name)
    }

    override fun enableVertexAttribArray(index: Int) {
        gl.enableVertexAttribArray(index)
    }

    override fun getUniformLocation(shaderProgram: ShaderProgram, name: String): Uniform {
        val location = gl.getUniformLocation(shaderProgram.program.delegate, name)
        location ?: throw RuntimeException("Uniform '$name' not created. Check that your shader include this uniform.")
        return Uniform(location)
    }

    override fun uniformMatrix4fv(uniform: Uniform, transpose: Boolean, data: Array<Float>) {
        gl.uniformMatrix4fv(uniform.uniformLocation, transpose, data)
    }

    override fun uniform1i(uniform: Uniform, data: Int) {
        gl.uniform1i(uniform.uniformLocation, data)
    }

    override fun uniform2i(uniform: Uniform, a: Int, b: Int) {
        gl.uniform2i(uniform.uniformLocation, a, b)
    }

    override fun uniform3i(uniform: Uniform, a: Int, b: Int, c: Int) {
        gl.uniform3i(uniform.uniformLocation, a, b, c)
    }

    override fun uniform1f(uniform: Uniform, first: Float) {
        gl.uniform1f(uniform.uniformLocation, first)
    }

    override fun uniform2f(uniform: Uniform, first: Float, second: Float) {
        gl.uniform2f(uniform.uniformLocation, first, second)
    }

    override fun uniform3f(uniform: Uniform, first: Float, second: Float, third: Float) {
        gl.uniform3f(uniform.uniformLocation, first, second, third)
    }

    override fun uniform4f(uniform: Uniform, first: Float, second: Float, third: Float, fourth: Float) {
        gl.uniform4f(uniform.uniformLocation, first, second, third, fourth)
    }

    override fun uniform1fv(uniform: Uniform, floats: Array<Float>) {
        gl.uniform1fv(uniform.uniformLocation, floats)
    }

    override fun uniform2fv(uniform: Uniform, floats: Array<Float>) {
        gl.uniform2fv(uniform.uniformLocation, floats)
    }

    override fun uniform3fv(uniform: Uniform, floats: Array<Float>) {
        gl.uniform3fv(uniform.uniformLocation, floats)
    }

    override fun uniform4fv(uniform: Uniform, floats: Array<Float>) {
        gl.uniform4fv(uniform.uniformLocation, floats)
    }

    override fun attachShader(shaderProgram: ShaderProgram, shader: Shader) {
        gl.attachShader(shaderProgram.program.delegate, shader.delegate)
    }

    override fun linkProgram(shaderProgram: ShaderProgram) {
        gl.linkProgram(shaderProgram.program.delegate)
    }

    override fun getString(parameterName: Int): String? {
        return gl.getParameter(parameterName) as? String
    }

    override fun getProgramParameter(shaderProgram: ShaderProgram, mask: ByteMask): Any {
        return gl.getProgramParameter(shaderProgram.program.delegate, mask)!!
    }

    override fun getShaderParameter(shader: Shader, mask: ByteMask): Any {
        return gl.getShaderParameter(shader.delegate, mask)!!
    }

    override fun createShader(type: ByteMask): Shader {
        return Shader(gl.createShader(type)!!)
    }

    override fun shaderSource(shader: Shader, source: String) {
        gl.shaderSource(shader.delegate, source)
    }

    override fun compileShader(shader: Shader) {
        gl.compileShader(shader.delegate)
    }

    override fun getShaderInfoLog(shader: Shader): String {
        return gl.getShaderInfoLog(shader.delegate) ?: ""
    }

    override fun deleteShader(shader: Shader) {
        gl.deleteShader(shader.delegate)
    }

    override fun getProgramInfoLog(shader: ShaderProgram): String {
        return gl.getProgramInfoLog(shader.program.delegate) ?: ""
    }

    override fun createBuffer(): Buffer {
        return Buffer(gl.createBuffer()!!)
    }

    override fun bindBuffer(target: ByteMask, buffer: Buffer) {
        gl.bindBuffer(target, buffer.delegate)
    }

    override fun bufferData(target: ByteMask, data: DataSource, usage: Int) {
        val converted = when (data) {
            is DataSource.FloatDataSource -> Float32Array(data.floats.toTypedArray())
            is DataSource.IntDataSource -> Uint32Array(data.ints.toTypedArray())
            is DataSource.ShortDataSource -> Uint16Array(data.shorts.toTypedArray())
            is DataSource.DoubleDataSource -> TODO("Not supported")
            is DataSource.UIntDataSource -> Uint32Array(data.ints.toTypedArray())
        }
        gl.bufferData(target, converted, usage)
    }

    override fun drawArrays(mask: ByteMask, offset: Int, vertexCount: Int) {
        gl.drawArrays(mask, offset, vertexCount)
    }

    override fun drawElements(mask: ByteMask, vertexCount: Int, type: Int, offset: Int) {
        gl.drawElements(mask, vertexCount, type, offset)
    }

    override fun viewport(x: Pixel, y: Pixel, width: Pixel, height: Pixel) {
        gl.viewport(x, y, width, height)
    }

    override fun createTexture(): TextureReference {
        return TextureReference(gl.createTexture()!!)
    }

    override fun createFrameBuffer(): FrameBufferReference {
        return FrameBufferReference(gl.createFramebuffer()!!)
    }

    override fun bindFrameBuffer(frameBufferReference: FrameBufferReference) {
        gl.bindFramebuffer(GL.FRAMEBUFFER, framebuffer = frameBufferReference.reference)
    }

    override fun frameBufferTexture2D(attachmentPoint: Int, textureReference: TextureReference, level: Int) {
        gl.framebufferTexture2D(GL.FRAMEBUFFER, attachmentPoint, GL.TEXTURE_2D, textureReference.reference, level)
    }

    override fun bindDefaultFrameBuffer() {
        gl.bindFramebuffer(GL.FRAMEBUFFER, null)
    }

    override fun createRenderBuffer(): RenderBufferReference {
        return RenderBufferReference(gl.createRenderbuffer()!!)
    }

    override fun bindRenderBuffer(renderBufferReference: RenderBufferReference) {
        gl.bindRenderbuffer(GL.RENDERBUFFER, renderBufferReference.reference)
    }

    override fun renderBufferStorage(internalformat: Int, width: Int, height: Int) {
        gl.renderbufferStorage(GL.RENDERBUFFER, internalformat, width, height)
    }

    override fun framebufferRenderbuffer(attachementType: Int, renderBufferReference: RenderBufferReference) {
        gl.framebufferRenderbuffer(GL.FRAMEBUFFER, attachementType, GL.RENDERBUFFER, renderBufferReference.reference)
    }

    override fun activeTexture(byteMask: ByteMask) {
        gl.activeTexture(byteMask)
    }

    override fun bindTexture(target: Int, textureReference: TextureReference) {
        gl.bindTexture(target, textureReference.reference)
    }

    override fun texImage2D(
        target: Int,
        level: Int,
        internalformat: Int,
        format: Int,
        type: Int,
        source: TextureImage
    ) {
        gl.texImage2D(target, level, internalformat, format, type, source.source)
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
        gl.texImage2D(target, level, internalformat, width, height, 0, format, type, Uint8Array(source.toTypedArray()))
    }

    override fun texParameteri(target: Int, paramName: Int, paramValue: Int) {
        gl.texParameteri(target, paramName, paramValue)
    }

    override fun generateMipmap(target: Int) {
        gl.generateMipmap(target)
    }
}
