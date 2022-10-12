package com.github.dwursteisen.minigdx.shaders.vertex

import com.github.dwursteisen.minigdx.render.RenderStage
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
        const int MAX_LIGHTS = ${RenderStage.MAX_LIGHTS};
        
        void main() {
            vec4 lightColor = vec4(0.0);
            vec3 n = normalize(aVertexNormal);
        
            for (int i = 0; i < MAX_LIGHTS; i++) {
                if(i >= uLightNumber) { break; }
                // Light computation
                vec3 lightDir = normalize(uLightPosition[i] - aVertexPosition);
                float diff = max(0.0, dot(n, lightDir));
             
                vec3 diffuse = diff * vec3(uLightColor[i]);
                
                float distance = length(uLightPosition[i] - aVertexPosition);
                vec3 radiance = vec3(uLightColor[i]) * uLightIntensity[i];
                float attenuation = uLightIntensity[i] / (distance * distance);
                radiance = radiance * attenuation;
                lightColor += vec4(diffuse + attenuation * vec3(uLightColor[i]), uLightColor[i].a);
            }
            
            gl_Position = uModelView * vec4(aVertexPosition, 1.0);
            vUVPosition = aUVPosition;
            vLightning = lightColor;
            
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
