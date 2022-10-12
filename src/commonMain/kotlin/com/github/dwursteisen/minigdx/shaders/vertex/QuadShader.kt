package com.github.dwursteisen.minigdx.shaders.vertex

import com.github.dwursteisen.minigdx.shaders.ShaderParameter

//language=GLSL
private val simpleVertexShader =
    """
        void main() {
            
            gl_Position = vec4(aVertexPosition, 0.0, 1.0);
            inputUV = vec2((aVertexPosition.x + 1.0) * 0.5, (aVertexPosition.y + 1.0) * 0.5);
        }
    """.trimIndent()

class QuadShader : VertexShader(simpleVertexShader) {

    val aVertexPosition = ShaderParameter.AttributeVec2("aVertexPosition")

    val inputUV = ShaderParameter.VaryingVec2("inputUV")

    override val parameters: List<ShaderParameter> = listOf(aVertexPosition, inputUV)
}
