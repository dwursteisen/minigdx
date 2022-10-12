package com.github.dwursteisen.minigdx.shaders.vertex

import com.github.dwursteisen.minigdx.render.RenderStage
import com.github.dwursteisen.minigdx.shaders.ShaderParameter

//language=GLSL
private val shader = """
    const int MAX_LIGHTS = ${RenderStage.MAX_LIGHTS}; 
    vec4 lightningColor(vec3 point) {
            vec4 lightColor = vec4(0.0);
            vec3 n = normalize(aVertexNormal);
            
            for (int i = 0; i < MAX_LIGHTS; i++) {
                if(i >= uLightNumber) { break; }
                // Light computation
                vec3 lightDir = normalize(uLightPosition[i] - point);
                float diff = max(0.0, dot(n, lightDir));
             
                vec3 diffuse = diff * vec3(uLightColor[i]);
                
                float distance = length(uLightPosition[i] - point);
                vec3 radiance = vec3(uLightColor[i]) * uLightIntensity[i];
                float attenuation = uLightIntensity[i] / (distance * distance);
                radiance = radiance * attenuation;
                lightColor += vec4(diffuse + attenuation * vec3(uLightColor[i]), uLightColor[i].a);
            } 
            return lightColor;
    }
    """
class LightColorVertexShader : VertexShader(shader) {
    val uLightPosition = ShaderParameter.UniformArrayVec3("uLightPosition", RenderStage.MAX_LIGHTS)
    val uLightColor = ShaderParameter.UniformArrayVec4("uLightColor", RenderStage.MAX_LIGHTS)
    val uLightIntensity = ShaderParameter.UniformArrayFloat("uLightIntensity", RenderStage.MAX_LIGHTS)
    val uLightNumber = ShaderParameter.UniformInt("uLightNumber")

    override val parameters: List<ShaderParameter> = listOf(
        uLightPosition,
        uLightColor,
        uLightIntensity,
        uLightNumber,
    )
}
