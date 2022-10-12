package com.github.dwursteisen.minigdx.shaders

import com.github.dwursteisen.minigdx.GL
import com.github.dwursteisen.minigdx.shaders.fragment.FragmentShader
import com.github.dwursteisen.minigdx.shaders.vertex.VertexShader

object ShaderUtils {

    fun createShaderProgram(gl: GL, vertexShader: VertexShader, fragmentShader: FragmentShader): ShaderProgram {
        val vertex = compileShader(gl, vertexShader, GL.VERTEX_SHADER)
        val fragment = compileShader(gl, fragmentShader, GL.FRAGMENT_SHADER)

        val shaderProgram = gl.createProgram()
        gl.attachShader(shaderProgram, vertex)
        gl.attachShader(shaderProgram, fragment)
        gl.linkProgram(shaderProgram)

        if (!gl.getProgramParameterB(shaderProgram, GL.LINK_STATUS)) {
            val log = gl.getProgramInfoLog(shaderProgram)
            throw RuntimeException("Shader compilation error: $log")
        }
        return shaderProgram
    }

    fun compileShader(gl: GL, vertexShader: ShaderCode, type: Int): Shader {
        val source = vertexShader.toString()
        val shader = gl.createShader(type)
        gl.shaderSource(shader, source)
        gl.compileShader(shader)

        if (!gl.getShaderParameterB(shader, GL.COMPILE_STATUS)) {
            val log = gl.getShaderInfoLog(shader)
            gl.deleteShader(shader)
            throw RuntimeException(
                "Shader generator class: ${vertexShader::class.simpleName}\n" +
                    "Shader compilation error: $log \n" +
                    "---------- \n" +
                    "Shader code in error: \n" +
                    source
            )
        }
        return shader
    }
}
