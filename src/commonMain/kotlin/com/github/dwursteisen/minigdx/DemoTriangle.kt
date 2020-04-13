package com.github.dwursteisen.minigdx

import com.github.dwursteisen.minigdx.entity.Camera
import com.github.dwursteisen.minigdx.entity.Colors
import com.github.dwursteisen.minigdx.entity.Landmark
import com.github.dwursteisen.minigdx.entity.Mesh
import com.github.dwursteisen.minigdx.entity.Vertice
import com.github.dwursteisen.minigdx.entity.delegate.Model
import com.github.dwursteisen.minigdx.graphics.clear
import com.github.dwursteisen.minigdx.math.Vector3
import com.github.dwursteisen.minigdx.shaders.DefaultShaders

@ExperimentalStdlibApi
class DemoTriangle : Game {

    override val worldSize = WorldSize(200, 200)

    private val camera = Camera.create(45, worldSize.ratio, 1, 100)

    private val program = DefaultShaders.create()

    private val model: Model = Model(
        mesh = Mesh(
            "model",
            vertices = arrayOf(
                Vertice(
                    position = Vector3(-0.5f, 0f, 0f),
                    color = Colors.RED
                ),
                Vertice(
                    position = Vector3(0.5f, 0f, 0f),
                    color = Colors.BLUE
                ),
                Vertice(
                    position = Vector3(0f, 0.5f, 0f),
                    color = Colors.GREEN
                )
            ),
            verticesOrder = shortArrayOf(
                0, 1, 2
            )
        )
    )

    @ExperimentalStdlibApi
    override fun create() {
        camera.translate(0, 0, -5)
    }

    private val landmark = Landmark.of()

    override fun render(delta: Seconds) {
        // --- draw ---
        clear(1 / 255f, 191 / 255f, 255 / 255f)

        program.render {
            camera.draw(it)
            model.draw(it)
            landmark.draw(it)
        }
    }
}
