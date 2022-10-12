package com.github.dwursteisen.minigdx.shaders.vertex

import com.github.dwursteisen.minigdx.render.RenderStage

//language=GLSL
private val shader = """
    const int MAX_LIGHTS = ${RenderStage.MAX_LIGHTS}; 
    vec4 lightningColor(
            vec3 point, 
            int uLightNumber, 
            vec3 uLightPosition[MAX_LIGHTS], 
            vec4 uLightColor[MAX_LIGHTS], 
            float uLightIntensity[MAX_LIGHTS]
    ) {
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
class LightColorVertexShader : VertexShader(shader)
