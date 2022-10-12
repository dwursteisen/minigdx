package com.github.dwursteisen.minigdx.shaders

/**
 * Shader code unit.
 *
 * The shader will be generated at runtime by aggregating all shader fragment code.
 */
abstract class ShaderCode(internal val shader: String) {

    /**
     * All parameters used in the Shader
     * All of the parameters will be declared in the header of the file.
     */
    open val parameters: List<ShaderParameter> = emptyList()

    /**
     * All shader code imported and use in this shader.
     *
     * Use to share code between different shader code.
     */
    open val imports: List<ShaderCode> = emptyList()

    override fun toString(): String {
        var tmpShader = """
                #ifdef GL_ES
                precision highp float;
                #endif
        """.trimIndent()

        val allImports = imports.flatMap { import -> import.imports + import }.toSet()

        val allParameters = allImports.flatMap { it.parameters } + parameters
        val uniforms: MutableList<ShaderParameter> = mutableListOf()
        val attributes: MutableList<ShaderParameter> = mutableListOf()
        val varyings: MutableList<ShaderParameter> = mutableListOf()
        allParameters.forEach {
            when (it) {
                is ShaderParameter.Uniform -> uniforms.add(it)
                is ShaderParameter.Attribute -> attributes.add(it)
                is ShaderParameter.Varying -> varyings.add(it)
                else -> throw IllegalArgumentException(
                    "Invalid type parameter! ${it::class.simpleName}. " +
                        "Expected to be Uniform or Attribute."
                )
            }
        }

        tmpShader += "\n"
        tmpShader += "\n// --- uniforms ---\n"
        tmpShader += uniforms.joinToString("\n", postfix = "\n")
        tmpShader += "\n// --- attributes ---\n"
        tmpShader += attributes.joinToString("\n", postfix = "\n")
        tmpShader += "\n// --- varyings ---\n"
        tmpShader += varyings.joinToString("\n", postfix = "\n")

        allImports.forEach { code ->
            tmpShader += "\n// Imported from ${code::class.simpleName}\n"
            tmpShader += code.shader
            tmpShader += "\n"
        }
        tmpShader += "\n"
        tmpShader += shader

        return tmpShader
    }
}
