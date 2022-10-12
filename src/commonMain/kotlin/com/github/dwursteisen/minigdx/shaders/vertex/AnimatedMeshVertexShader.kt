package com.github.dwursteisen.minigdx.shaders.vertex

import com.github.dwursteisen.minigdx.shaders.ShaderCode
import com.github.dwursteisen.minigdx.shaders.ShaderParameter
import com.github.dwursteisen.minigdx.shaders.ShaderParameter.AttributeVec2
import com.github.dwursteisen.minigdx.shaders.ShaderParameter.AttributeVec3
import com.github.dwursteisen.minigdx.shaders.ShaderParameter.AttributeVec4
import com.github.dwursteisen.minigdx.shaders.ShaderParameter.UniformArrayMat4
import com.github.dwursteisen.minigdx.shaders.ShaderParameter.UniformMat4

//language=GLSL
private val shader: String =
    """
        const int MAX_WEIGHTS = 4;
        void main() {
            vec4 totalLocalPos = vec4(0.0);
            vec4 totalNormalPos = vec4(0.0);
            
            for(int i=0;i<MAX_WEIGHTS;i++){
                int joinId = int(aJoints[i]);
                mat4 uJointMatrix = uJointTransformationMatrix[joinId];
                vec4 posePosition = uJointMatrix * vec4(aVertexPosition, 1.0);
                vec4 poseNormal = uJointMatrix * vec4(aVertexNormal, 1.0);
                
                // For an unknown reason, no evaluating aWeights make the 
                // computation not working on some devise (ie: reproduced on an Android TV).
                // This check is useless but make the things works!
                if(aWeights[i] >= 0.0) {
                    totalLocalPos += posePosition * aWeights[i];
                    totalNormalPos += poseNormal * aWeights[i];
                }
            }
        
            gl_Position = uModelView * totalLocalPos;
            
            vUVPosition = aUVPosition;
            vLightning = lightningColor(totalLocalPos.xyz);
        }
"""

class AnimatedMeshVertexShader(maxJoints: Int) : VertexShader(shader) {

    private val lightColorVertexShader = LightColorVertexShader()

    val uModelView = UniformMat4("uModelView")
    val uJointTransformationMatrix = UniformArrayMat4("uJointTransformationMatrix", maxJoints)

    val uLightPosition = lightColorVertexShader.uLightPosition
    val uLightColor = lightColorVertexShader.uLightColor
    val uLightIntensity = lightColorVertexShader.uLightIntensity
    val uLightNumber = lightColorVertexShader.uLightNumber

    val aVertexPosition = AttributeVec3("aVertexPosition")
    val aVertexNormal = AttributeVec3("aVertexNormal")
    val aJoints = AttributeVec4("aJoints")
    val aWeights = AttributeVec4("aWeights")
    val aUVPosition = AttributeVec2("aUVPosition")

    private val vUVPosition = ShaderParameter.VaryingVec2("vUVPosition")
    private val vLightning = ShaderParameter.VaryingVec4("vLightning")

    override val imports: List<ShaderCode> = listOf(lightColorVertexShader)

    override val parameters: List<ShaderParameter> = listOf(
        uModelView,
        uJointTransformationMatrix,
        aVertexPosition,
        aVertexNormal,
        aUVPosition,
        aJoints,
        aWeights,
        vUVPosition,
        vLightning,
    )
}
