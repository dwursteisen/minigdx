package com.github.dwursteisen.minigdx.shaders

import kotlin.test.Test
import kotlin.test.assertEquals

class ShaderCodeTest {

    class MainShader : ShaderCode("") {

        val myUniform = ShaderParameter.UniformInt("myUniform")
        val myArrayUniform = ShaderParameter.UniformArrayVec3("myArrayUniform", 50)
        val myAttribute = ShaderParameter.AttributeVec2("myAttribute")
        val myVarying = ShaderParameter.VaryingFloat("myVarying")

        override val parameters: List<ShaderParameter> = listOf(
            myUniform,
            myAttribute,
            myArrayUniform,
            myVarying
        )
    }

    @Test
    fun generateParameters() {
        val shader = MainShader().toString()

        assertEquals(
            """
            #ifdef GL_ES
            precision highp float;
            #endif
            
            // --- uniforms ---
            uniform int myUniform;
            uniform vec3 myArrayUniform[50];
            
            // --- attributes ---
            attribute vec2 myAttribute;
            
            // --- varyings ---
            varying float myVarying;
            """.trimIndent().trim(),
            shader.trimIndent().trim()
        )
    }

    class ShaderWithFunction : ShaderCode(
        """
        void circle()
        {
        // function code goes here
        }
        """.trimIndent()
    ) {

        val myAttribute = ShaderParameter.AttributeVec2("myOtherAttribute")

        override val parameters: List<ShaderParameter> = listOf(myAttribute)
    }
    class MyCompositeMainShader : ShaderCode("void main() { // TODO }") {

        override val imports: List<ShaderCode> = listOf(ShaderWithFunction())

        val myAttribute = ShaderParameter.AttributeVec2("myAttribute")

        override val parameters: List<ShaderParameter> = listOf(
            myAttribute,
        )
    }
    @Test
    fun generateCompositeShader() {
        val shader = MyCompositeMainShader().toString()

        assertEquals(
            """
            #ifdef GL_ES
            precision highp float;
            #endif
            
            // --- uniforms ---
            
            
            // --- attributes ---
            attribute vec2 myOtherAttribute;
            attribute vec2 myAttribute;
            
            // --- varyings ---
            
            
            // Imported from ShaderWithFunction
            void circle()
            {
            // function code goes here
            }
        
            void main() { // TODO }
            """.trimIndent().trim(),
            shader.trimIndent().trim()
        )
    }

    class MyCompositeMainShaderWithImport : ShaderCode("void parentShader() { // TODO }") {

        override val imports: List<ShaderCode> = listOf(MyCompositeMainShader())

        val myOriginalAttribute = ShaderParameter.AttributeVec2("myOriginalAttribute")

        override val parameters: List<ShaderParameter> = listOf(
            myOriginalAttribute,
        )
    }

    @Test
    fun generateCompositeShaderWithImport() {
        val shader = MyCompositeMainShaderWithImport().toString()

        assertEquals(
            """
            #ifdef GL_ES
            precision highp float;
            #endif
            
            // --- uniforms ---
            
            
            // --- attributes ---
            attribute vec2 myOtherAttribute;
            attribute vec2 myAttribute;
            attribute vec2 myOriginalAttribute;
            
            // --- varyings ---
            
            
            // Imported from ShaderWithFunction
            void circle()
            {
            // function code goes here
            }
        
            // Imported from MyCompositeMainShader
            void main() { // TODO }
            
            void parentShader() { // TODO }
            """.trimIndent().trim(),
            shader.trimIndent().trim()
        )
    }
}
