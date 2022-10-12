package com.github.dwursteisen.minigdx.shaders.vertex

import com.github.dwursteisen.minigdx.shaders.ShaderCode
import com.github.dwursteisen.minigdx.shaders.ShaderParameter
import com.github.dwursteisen.minigdx.shaders.ShaderParameter.AttributeVec2
import com.github.dwursteisen.minigdx.shaders.ShaderParameter.AttributeVec3
import com.github.dwursteisen.minigdx.shaders.ShaderParameter.UniformMat4

//language=GLSL
private val simpleVertexShader =
    """
        void main() {
            gl_Position = uModelView * vec4(aVertexPosition, 1.0);
            vUVPosition = aUVPosition;
            vLightning = lightningColor(aVertexPosition);
            
        }
    """.trimIndent()

class MeshVertexShader : VertexShader(
    shader = simpleVertexShader
) {
    private val lightColorVertexShader = LightColorVertexShader()

    val uModelView = UniformMat4("uModelView")
    val aVertexPosition = AttributeVec3("aVertexPosition")
    val aVertexNormal = AttributeVec3("aVertexNormal")
    val aUVPosition = AttributeVec2("aUVPosition")

    val uLightPosition = lightColorVertexShader.uLightPosition
    val uLightColor = lightColorVertexShader.uLightColor
    val uLightIntensity = lightColorVertexShader.uLightIntensity
    val uLightNumber = lightColorVertexShader.uLightNumber

    private val vUVPosition = ShaderParameter.VaryingVec2("vUVPosition")
    private val vLightning = ShaderParameter.VaryingVec4("vLightning")

    override val imports: List<ShaderCode> = listOf(lightColorVertexShader)

    override val parameters: List<ShaderParameter> = listOf(
        uModelView,
        aVertexPosition,
        aVertexNormal,
        aUVPosition,
        vUVPosition,
        vLightning,
    )
}
