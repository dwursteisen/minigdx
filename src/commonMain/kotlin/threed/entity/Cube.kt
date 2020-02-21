package threed.entity

import threed.graphics.Render
import threed.math.Vector3
import threed.shaders.ShaderProgram

data class Cube(
    val mesh: Mesh,
    val render: Render = Render(mesh)
) : Drawable {

    constructor(name: String, color: Color = Colors.WHITE) : this(
        Mesh(
            name = name,
            vertices = arrayOf(
                Vertice(position = Vector3(x = -1, y = 1, z = 1), color = color), // 0
                Vertice(position = Vector3(1, 1, 1), color = color), // 1
                Vertice(position = Vector3(-1, -1, 1), color = color), // 2
                Vertice(position = Vector3(1, -1, 1), color = color), // 3
                Vertice(position = Vector3(-1, -1, -1), color = color), // 4
                Vertice(position = Vector3(-1, 1, -1), color = color), // 5
                Vertice(position = Vector3(1, 1, -1), color = color), // 6
                Vertice(position = Vector3(1, -1, -1), color = color) // 7
            ),
            verticesOrder = shortArrayOf(
                // front
                0, 1, 2,
                2, 1, 3,
                // left
                0, 2, 5,
                5, 2, 4,
                // right
                1, 3, 7,
                7, 1, 6,
                // back
                7, 6, 5,
                5, 7, 4,
                // up
                0, 1, 5,
                1, 5, 6,
                // down
                2, 3, 4,
                3, 4, 7
            )
        )
    )

    override fun draw(program: ShaderProgram) {
        render.draw(program)
    }
}
