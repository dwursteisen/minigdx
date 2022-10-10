package com.github.dwursteisen.minigdx.graphics

import com.github.dwursteisen.minigdx.ByteMask
import com.github.dwursteisen.minigdx.GL
import com.github.dwursteisen.minigdx.Percent
import com.github.dwursteisen.minigdx.file.TextureImage
import com.github.dwursteisen.minigdx.shaders.Buffer
import com.github.dwursteisen.minigdx.shaders.DataSource
import com.github.dwursteisen.minigdx.shaders.FrameBufferReference
import com.github.dwursteisen.minigdx.shaders.RenderBufferReference
import com.github.dwursteisen.minigdx.shaders.Shader
import com.github.dwursteisen.minigdx.shaders.ShaderProgram
import com.github.dwursteisen.minigdx.shaders.TextureReference
import com.github.dwursteisen.minigdx.shaders.Uniform

data class ViewportCall(val x: Int, val y: Int, val width: Int, val height: Int)

class MockGL : GL {

    lateinit var viewportCall: ViewportCall

    override fun clearColor(r: Percent, g: Percent, b: Percent, a: Percent) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun clear(mask: ByteMask) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun clearDepth(depth: Number) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun enable(mask: ByteMask) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun disable(mask: ByteMask) {
        TODO("Not yet implemented")
    }

    override fun blendFunc(sfactor: ByteMask, dfactor: ByteMask) {
        TODO("Not yet implemented")
    }

    override fun createProgram(): ShaderProgram {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getAttribLocation(shaderProgram: ShaderProgram, name: String): Int {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getUniformLocation(shaderProgram: ShaderProgram, name: String): Uniform {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun attachShader(shaderProgram: ShaderProgram, shader: Shader) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun linkProgram(shaderProgram: ShaderProgram) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getProgramParameter(shaderProgram: ShaderProgram, mask: ByteMask): Any {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getString(parameterName: Int): String? {
        TODO("Not yet implemented")
    }

    override fun getShaderParameter(shader: Shader, mask: ByteMask): Any {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun createShader(type: ByteMask): Shader {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun shaderSource(shader: Shader, source: String) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun compileShader(shader: Shader) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getShaderInfoLog(shader: Shader): String {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteShader(shader: Shader) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getProgramInfoLog(shader: ShaderProgram): String {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun createBuffer(): Buffer {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun createFrameBuffer(): FrameBufferReference {
        TODO("Not yet implemented")
    }

    override fun bindFrameBuffer(frameBufferReference: FrameBufferReference) {
        TODO("Not yet implemented")
    }

    override fun bindDefaultFrameBuffer() {
        TODO("Not yet implemented")
    }

    override fun createRenderBuffer(): RenderBufferReference {
        TODO("Not yet implemented")
    }

    override fun bindRenderBuffer(renderBufferReference: RenderBufferReference) {
        TODO("Not yet implemented")
    }

    override fun renderBufferStorage(internalformat: Int, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun framebufferRenderbuffer(attachementType: Int, renderBufferReference: RenderBufferReference) {
        TODO("Not yet implemented")
    }

    override fun frameBufferTexture2D(attachmentPoint: Int, textureReference: TextureReference, level: Int) {
        TODO("Not yet implemented")
    }

    override fun bindBuffer(target: ByteMask, buffer: Buffer) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun bufferData(target: ByteMask, data: DataSource, usage: Int) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun depthFunc(target: ByteMask) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun vertexAttribPointer(
        index: Int,
        size: Int,
        type: Int,
        normalized: Boolean,
        stride: Int,
        offset: Int
    ) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun enableVertexAttribArray(index: Int) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun useProgram(shaderProgram: ShaderProgram) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun createTexture(): TextureReference {
        TODO("Not yet implemented")
    }

    override fun activeTexture(byteMask: ByteMask) {
        TODO("Not yet implemented")
    }

    override fun bindTexture(target: Int, textureReference: TextureReference) {
        TODO("Not yet implemented")
    }

    override fun uniformMatrix4fv(uniform: Uniform, transpose: Boolean, data: Array<Float>) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun uniform1i(uniform: Uniform, data: Int) {
        TODO("Not yet implemented")
    }

    override fun uniform2i(uniform: Uniform, a: Int, b: Int) {
        TODO("Not yet implemented")
    }

    override fun uniform3i(uniform: Uniform, a: Int, b: Int, c: Int) {
        TODO("Not yet implemented")
    }

    override fun uniform1f(uniform: Uniform, first: Float) {
        TODO("Not yet implemented")
    }

    override fun uniform2f(uniform: Uniform, first: Float, second: Float) {
        TODO("Not yet implemented")
    }

    override fun uniform3f(uniform: Uniform, first: Float, second: Float, third: Float) {
        TODO("Not yet implemented")
    }

    override fun uniform4f(uniform: Uniform, first: Float, second: Float, third: Float, fourth: Float) {
        TODO("Not yet implemented")
    }

    override fun uniform1fv(uniform: Uniform, floats: Array<Float>) {
        TODO("Not yet implemented")
    }

    override fun uniform2fv(uniform: Uniform, floats: Array<Float>) {
        TODO("Not yet implemented")
    }

    override fun uniform3fv(uniform: Uniform, floats: Array<Float>) {
        TODO("Not yet implemented")
    }

    override fun uniform4fv(uniform: Uniform, floats: Array<Float>) {
        TODO("Not yet implemented")
    }

    override fun drawArrays(mask: ByteMask, offset: Int, vertexCount: Int) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun drawElements(mask: ByteMask, vertexCount: Int, type: Int, offset: Int) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun viewport(x: Int, y: Int, width: Int, height: Int) {
        viewportCall = ViewportCall(x, y, width, height)
    }

    override fun texImage2D(
        target: Int,
        level: Int,
        internalformat: Int,
        format: Int,
        type: Int,
        source: TextureImage
    ) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun texParameteri(target: Int, paramName: Int, paramValue: Int) {
        TODO("Not yet implemented")
    }

    override fun generateMipmap(target: Int) {
        TODO("Not yet implemented")
    }
}
