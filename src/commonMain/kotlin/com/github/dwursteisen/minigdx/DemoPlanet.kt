package com.github.dwursteisen.minigdx

import com.curiouscreature.kotlin.math.inverse
import com.curiouscreature.kotlin.math.transpose
import com.github.dwursteisen.minigdx.entity.Camera
import com.github.dwursteisen.minigdx.entity.Landmark
import com.github.dwursteisen.minigdx.file.MeshReader
import com.github.dwursteisen.minigdx.graphics.Render
import com.github.dwursteisen.minigdx.input.TouchSignal
import com.github.dwursteisen.minigdx.shaders.DefaultShaders

class DemoPlanet : Game {

    override val worldSize = WorldSize(200, 200)

    private val camera = Camera.create(45, worldSize.ratio, 1, 1000)

    private val program = DefaultShaders.create()

    private val landmark = Landmark.of()
    private var model: Render? = null

    @ExperimentalStdlibApi
    override fun create() {

        camera.translate(0, 0, -200)

        fileHandler.readData("planet.protobuf").onLoaded {
            val fromProtobuf = MeshReader.fromProtobuf(it)
            val (mesh, armature, animations) = fromProtobuf

            model = Render(mesh)
        }
    }

    var rotationStart: Float? = null
    var currentRotation: Float = 0f
    var xStart = 0f

    override fun render(delta: Seconds) {
        if (!fileHandler.isLoaded) {
            return
        }

        // --- act ---
        val cursor = inputs.isTouched(TouchSignal.TOUCH1)
        if (cursor != null) {
            if (rotationStart == null) {
                rotationStart = model?.mesh?.rotation?.y ?: 0f
                xStart = cursor.x
            }

            val screenWidth = gl.screen.width
            val factor = (cursor.x - screenWidth * 0.5f) / screenWidth
            currentRotation = factor * 180f
            rotationStart?.run {
                model?.mesh?.setRotationY(this + currentRotation)
            }
        } else {
            rotationStart = null
            model?.mesh?.rotateY(delta * 10)
        }

        val modelMatrix = camera.modelMatrix
        val normalMatrix = transpose(inverse(modelMatrix))
        // --- draw ---
        // TODO: create clear method
        gl.clearColor(1 / 255f, 191 / 255f, 255 / 255f, 1f)
        gl.clearDepth(1.0)
        gl.enable(GL.DEPTH_TEST)
        gl.depthFunc(GL.LEQUAL)
        gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)

        // TODO: create program.draw { … }
        gl.useProgram(program)

        gl.uniformMatrix4fv(program.getUniform("uProjectionMatrix"), false, camera.projectionMatrix)
        gl.uniformMatrix4fv(program.getUniform("uViewMatrix"), false, camera.modelMatrix)
        gl.uniformMatrix4fv(program.getUniform("uNormalMatrix"), false, normalMatrix)

        model?.draw(program)
        landmark.draw(program)
    }
}
