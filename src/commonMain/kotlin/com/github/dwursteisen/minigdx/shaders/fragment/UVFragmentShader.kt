package com.github.dwursteisen.minigdx.shaders.fragment

import com.github.dwursteisen.minigdx.shaders.ShaderParameter

//language=GLSL
private val simpleFragmentShader =
    """
        void main() {
              vec4 texel = texture2D(uUV, vUVPosition);
              // If the light alpha is 0, the light will be vec3(1.0) so the texel will not be altered
              // otherwise, the light will be vLighting.rgb
              vec3 light = (vec3(1.0) * (vec3(1.0) - vec3(vLightning.a))) + vLightning.rgb * vLightning.a;
              gl_FragColor = vec4(texel.rgb * light, texel.a);
        }
    """.trimIndent()

class UVFragmentShader : FragmentShader(simpleFragmentShader) {

    val uUV = ShaderParameter.UniformSample2D("uUV")
    private val vLightning = ShaderParameter.VaryingVec4("vLightning")
    private val vUVPosition = ShaderParameter.VaryingVec2("vUVPosition")

    override val parameters: List<ShaderParameter> = listOf(uUV, vUVPosition, vLightning)
}
