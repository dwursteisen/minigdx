package com.github.dwursteisen.minigdx.shaders.vertex

import com.github.dwursteisen.minigdx.render.RenderStage
import com.github.dwursteisen.minigdx.shaders.ShaderCode
import com.github.dwursteisen.minigdx.shaders.ShaderParameter
import com.github.dwursteisen.minigdx.shaders.ShaderParameter.AttributeVec2
import com.github.dwursteisen.minigdx.shaders.ShaderParameter.AttributeVec3
import com.github.dwursteisen.minigdx.shaders.ShaderParameter.UniformArrayFloat
import com.github.dwursteisen.minigdx.shaders.ShaderParameter.UniformArrayVec3
import com.github.dwursteisen.minigdx.shaders.ShaderParameter.UniformArrayVec4
import com.github.dwursteisen.minigdx.shaders.ShaderParameter.UniformMat4

//language=GLSL
private val simpleVertexShader =
    """
        void main() {
            gl_Position = uModelView * vec4(aVertexPosition, 1.0);
            vUVPosition = aUVPosition;
            vLightning = lightningColor(aVertexPosition, uLightNumber, uLightPosition, uLightColor, uLightIntensity);
            
        }
    """.trimIndent()

class MeshVertexShader : VertexShader(
    shader = simpleVertexShader
) {
    val uModelView = UniformMat4("uModelView")
    val aVertexPosition = AttributeVec3("aVertexPosition")
    val aVertexNormal = AttributeVec3("aVertexNormal")
    val aUVPosition = AttributeVec2("aUVPosition")

    val uLightPosition = UniformArrayVec3("uLightPosition", RenderStage.MAX_LIGHTS)
    val uLightColor = UniformArrayVec4("uLightColor", RenderStage.MAX_LIGHTS)
    val uLightIntensity = UniformArrayFloat("uLightIntensity", RenderStage.MAX_LIGHTS)
    val uLightNumber = ShaderParameter.UniformInt("uLightNumber")

    private val vUVPosition = ShaderParameter.VaryingVec2("vUVPosition")
    private val vLightning = ShaderParameter.VaryingVec4("vLightning")

    override val imports: List<ShaderCode> = listOf(LightColorVertexShader())

    override val parameters: List<ShaderParameter> = listOf(
        uModelView,
        aVertexPosition,
        aVertexNormal,
        aUVPosition,
        uLightPosition,
        uLightColor,
        uLightIntensity,
        uLightNumber,
        vUVPosition,
        vLightning,
    )
}
