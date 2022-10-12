package com.github.dwursteisen.minigdx.shaders.fragment

import com.github.dwursteisen.minigdx.shaders.ShaderParameter

//language=GLSL
private val simpleFragmentShader =
    """
        void main() {
              gl_FragColor = vColor;
        }
    """.trimIndent()

class ColorFragmentShader : FragmentShader(simpleFragmentShader) {

    private val vColor = ShaderParameter.VaryingVec4("vColor")

    override val parameters: List<ShaderParameter> = listOf(vColor)
}
