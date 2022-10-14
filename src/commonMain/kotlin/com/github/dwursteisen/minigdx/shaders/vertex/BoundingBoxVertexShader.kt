package com.github.dwursteisen.minigdx.shaders.vertex

import com.github.dwursteisen.minigdx.shaders.ShaderParameter

//language=GLSL
private val simpleVertexShader =
    """
        void main() {
            gl_Position = uModelView * vec4(aVertexPosition, 1.0);
            vColor = uColor;
        }
    """.trimIndent()

class BoundingBoxVertexShader : VertexShader(simpleVertexShader) {

    val uModelView =
        ShaderParameter.UniformMat4("uModelView")
    val uColor = ShaderParameter.UniformVec4("uColor")

    val aVertexPosition =
        ShaderParameter.AttributeVec3("aVertexPosition")

    private val vColor = ShaderParameter.VaryingVec4("vColor")

    override val parameters: List<ShaderParameter> = listOf(
        uModelView,
        uColor,
        aVertexPosition,
        vColor
    )
}
