package com.github.dwursteisen.minigdx.ecs.components.position

import ModelFactory.gameContext
import com.github.dwursteisen.minigdx.ecs.Engine
import com.github.dwursteisen.minigdx.ecs.components.Position
import com.github.dwursteisen.minigdx.ecs.components.assertEquals
import com.github.dwursteisen.minigdx.ecs.entities.position
import com.github.dwursteisen.minigdx.math.ImmutableVector3
import kotlin.test.Test

class MovableTest {

    private val engine = Engine(gameContext())

    @Test
    fun itMoveEntity() {
        val entity = engine.create {
            add(Position())
        }
        val move = Moveable(entity, ImmutableVector3(10f, 10f, 10f), 10f)

        // half of the move
        move.update(5f)

        assertEquals(5f, entity.position.translation.x)
        assertEquals(5f, entity.position.translation.y)
        assertEquals(5f, entity.position.translation.z)

        // finish the move
        move.update(5f)
        assertEquals(10f, entity.position.translation.x)
        assertEquals(10f, entity.position.translation.y)
        assertEquals(10f, entity.position.translation.z)
    }
}
