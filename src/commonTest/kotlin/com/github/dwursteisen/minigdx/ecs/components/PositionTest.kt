package com.github.dwursteisen.minigdx.ecs.components

import MockPlatformContext
import com.curiouscreature.kotlin.math.Quaternion
import com.github.dwursteisen.minigdx.GameContext
import com.github.dwursteisen.minigdx.Resolution
import com.github.dwursteisen.minigdx.ecs.Engine
import com.github.dwursteisen.minigdx.ecs.entities.Entity
import com.github.dwursteisen.minigdx.ecs.entities.position
import com.github.dwursteisen.minigdx.math.Vector3
import createGameConfiguration
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.fail

fun assertEquals(
    expected: Float,
    actual: Float,
    delta: Float = 0.001f,
    message: String = "$expected != $actual (𝝙 $delta)"
) {
    if (abs(expected - actual) > delta) {
        fail(message)
    }
}

fun assertEquals(
    expected: Quaternion,
    actual: Quaternion,
    delta: Float = 0.001f,
    message: String = "$expected != $actual (𝝙 $delta)"
) {
    try {
        assertEquals(expected.x, actual.x, delta)
        assertEquals(expected.y, actual.y, delta)
        assertEquals(expected.z, actual.z, delta)
        assertEquals(expected.w, actual.w, delta)
    } catch (ex: Exception) {
        fail(message)
    }
}

class PositionTest {

    private val engine = Engine(
        GameContext(
            MockPlatformContext(createGameConfiguration()),
            Resolution(100, 100)
        )
    )

    private fun createEntities(): Pair<Entity, Entity> {
        val parent = engine.create {
            named("parent")
            add(Position())
        }

        val child = engine.create {
            named("child")
            add(Position())
        }

        child.attachTo(parent)
        return parent to child
    }

    @Test
    fun translation_add_global_translation() {
        val (parent, child) = createEntities()
        parent.position
            .addLocalScale(x = 2f)
            .addGlobalTranslation(x = 5)
        assertEquals(5f, parent.position.translation.x)
        assertEquals(5f, child.position.translation.x)
        assertEquals(0f, child.position.localTranslation.x)
        child.position.addGlobalTranslation(x = 5)
        assertEquals(5f, parent.position.translation.x)
        assertEquals(10f, child.position.translation.x)
        assertEquals(1.6666f, child.position.localTranslation.x)
    }

    @Test
    fun translation_add_local_translation() {
        val (parent, child) = createEntities()
        parent.position
            .addLocalScale(x = 2f)
            .addLocalTranslation(x = 5)
        assertEquals(5f, parent.position.translation.x)
        assertEquals(5f, child.position.translation.x)
        assertEquals(0f, child.position.localTranslation.x)
        child.position.addLocalTranslation(x = 5)
        assertEquals(5f, parent.position.translation.x)
        assertEquals(20f, child.position.translation.x)
        assertEquals(5f, child.position.localTranslation.x)
    }

    @Test
    fun translation_set_global_translation() {
        val (parent, child) = createEntities()
        parent.position.setGlobalTranslation(x = 5)
        assertEquals(5f, parent.position.translation.x)
        assertEquals(5f, child.position.translation.x)
        assertEquals(0f, child.position.localTranslation.x)
        child.position.setGlobalTranslation(x = 15)
        assertEquals(5f, parent.position.translation.x)
        assertEquals(15f, child.position.translation.x)
        assertEquals(10f, child.position.localTranslation.x)
    }

    @Test
    fun translation_set_local_translation() {
        val (parent, child) = createEntities()
        parent.position.setLocalTranslation(x = 5)
        assertEquals(5f, parent.position.translation.x)
        assertEquals(5f, child.position.translation.x)
        assertEquals(0f, child.position.localTranslation.x)
        child.position.setLocalTranslation(x = 15)
        assertEquals(5f, parent.position.translation.x)
        assertEquals(20f, child.position.translation.x)
        assertEquals(15f, child.position.localTranslation.x)
    }

    @Test
    fun rotation_add_local_rotation() {
        val (parent, child) = createEntities()
        parent.position.addLocalRotation(x = 90f)
        assertEquals(90f, parent.position.rotation.x)
        assertEquals(90f, child.position.rotation.x)
        assertEquals(0f, child.position.localRotation.x)
        child.position.addLocalRotation(x = 45f)
        assertEquals(90f, parent.position.rotation.x)
        assertEquals(135f, child.position.rotation.x)
        assertEquals(45f, child.position.localRotation.x)
    }

    @Test
    fun rotation_add_local_rotation_y_up_to_360_degrees() {
        val (parent, _) = createEntities()
        parent.position.addLocalRotation(y = 90f)
        assertEquals(Quaternion(0f, 0.707f, 0f, 0.707f), parent.position.localQuaternion)
        parent.position.addLocalRotation(y = 90f)
        assertEquals(Quaternion(0f, 1f, 0f, 0f), parent.position.localQuaternion)
        parent.position.addLocalRotation(y = 90f)
        assertEquals(Quaternion(0f, 0.707f, 0f, -0.707f), parent.position.localQuaternion)
        parent.position.addLocalRotation(y = 90f)
        assertEquals(Quaternion(0f, 0f, 0f, -1f), parent.position.localQuaternion)
    }

    @Test
    fun rotation_add_local_rotation_x_up_to_360_degrees() {
        val (parent, _) = createEntities()
        parent.position.addLocalRotation(x = 90f)
        assertEquals(Quaternion(0.707f, 0f, 0f, 0.707f), parent.position.localQuaternion)
        parent.position.addLocalRotation(x = 90f)
        assertEquals(Quaternion(1f, 0f, 0f, 0f), parent.position.localQuaternion)
        parent.position.addLocalRotation(x = 90f)
        assertEquals(Quaternion(0.707f, 0f, 0f, -0.707f), parent.position.localQuaternion)
        parent.position.addLocalRotation(x = 90f)
        assertEquals(Quaternion(0f, 0f, 0f, -1f), parent.position.localQuaternion)
    }

    @Test
    fun rotation_add_local_rotation_z_up_to_360_degrees() {
        val (parent, _) = createEntities()
        parent.position.addLocalRotation(z = 90f)
        assertEquals(Quaternion(0f, 0f, 0.707f, 0.707f), parent.position.localQuaternion)
        parent.position.addLocalRotation(z = 90f)
        assertEquals(Quaternion(0f, 0f, 1f, 0f), parent.position.localQuaternion)
        parent.position.addLocalRotation(z = 90f)
        assertEquals(Quaternion(0f, 0f, 0.707f, -0.707f), parent.position.localQuaternion)
        parent.position.addLocalRotation(z = 90f)
        assertEquals(Quaternion(0f, 0f, 0f, -1f), parent.position.localQuaternion)
    }

    @Test
    fun rotation_set_local_rotation() {
        val (parent, child) = createEntities()
        parent.position.setLocalRotation(x = 90)
        assertEquals(90f, parent.position.rotation.x)
        assertEquals(90f, child.position.rotation.x)
        assertEquals(0f, child.position.localRotation.x)
        child.position.setLocalRotation(x = 45)
        assertEquals(90f, parent.position.rotation.x)
        assertEquals(135f, child.position.rotation.x)
        assertEquals(45f, child.position.localRotation.x)
    }

    @Test
    fun scale_add_local_scale() {
        val (parent, child) = createEntities()
        parent.position.addLocalScale(x = 1f)
        assertEquals(2f, parent.position.scale.x)
        assertEquals(2f, child.position.scale.x)
        assertEquals(1f, child.position.localScale.x)
        child.position.addLocalScale(x = 1f)
        assertEquals(2f, parent.position.scale.x)
        assertEquals(4f, child.position.scale.x)
        assertEquals(2f, child.position.localScale.x)
    }

    @Test
    fun scale_set_local_scale() {
        val (parent, child) = createEntities()
        parent.position.setLocalScale(x = 2f)
        assertEquals(2f, parent.position.scale.x)
        assertEquals(2f, child.position.scale.x)
        assertEquals(1f, child.position.localScale.x)
        child.position.setLocalScale(x = 4f)
        assertEquals(2f, parent.position.scale.x)
        assertEquals(8f, child.position.scale.x)
        assertEquals(4f, child.position.localScale.x)
    }

    @Test
    fun rotation_around() {
        val position = Position()
        position.setGlobalTranslation(x = 20)
        position.addRotationAround(Vector3(x = 10f), z = 90f)
        assertEquals(10f, position.translation.x)
        assertEquals(10f, position.translation.y)
        assertEquals(0f, position.translation.z)
        position.addRotationAround(Vector3(x = 10f), z = 90f)
        assertEquals(0f, position.translation.x)
        assertEquals(0f, position.translation.y)
        assertEquals(0f, position.translation.z)
        position.addRotationAround(Vector3(x = 10f), z = -180f)
        assertEquals(20f, position.translation.x)
        assertEquals(0f, position.translation.y)
        assertEquals(0f, position.translation.z)
    }

    @Test
    fun simulation_rollback() {
        val position = Position()
        val result: Any = position.simulation {
            addLocalScale(1.0f, 1.0f, 1.0f)
            rollback()
        }
        assertSame(Unit, result)
        assertEquals(1f, position.scale.x)
    }

    @Test
    fun simulation_commit() {
        val position = Position()
        val result: Any = position.simulation {
            addLocalScale(1.0f, 1.0f, 1.0f)
            commit()
        }
        assertSame(Unit, result)
        assertEquals(2f, position.scale.x)
    }

    @Test
    fun moveTest() {
        with(Position()) {
            move(x = 10f)
            assertEquals(10f, translation.x)
            assertEquals(0f, translation.y)
            assertEquals(0f, translation.z)
        }

        with(Position()) {
            addLocalRotation(y = 90f)
            move(x = 10f)
            assertEquals(0f, translation.x)
            assertEquals(0f, translation.y)
            assertEquals(-10f, translation.z)
        }
    }
}
