package com.github.dwursteisen.minigdx.ecs.components.position

import com.github.dwursteisen.minigdx.Seconds
import com.github.dwursteisen.minigdx.ecs.components.Component
import com.github.dwursteisen.minigdx.ecs.entities.Entity
import com.github.dwursteisen.minigdx.ecs.entities.position
import com.github.dwursteisen.minigdx.math.ImmutableVector3
import com.github.dwursteisen.minigdx.math.Interpolation
import com.github.dwursteisen.minigdx.math.Interpolations
import com.github.dwursteisen.minigdx.math.Vector2
import com.github.dwursteisen.minigdx.math.Vector3

class TweenFactoryComponent : Component {

    val generatedTweens: MutableList<Tween<*>> = mutableListOf()

    override fun onRemoved(entity: Entity) {
        generatedTweens.clear()
    }

    fun <T> tween(
        seed: T,
        startValues: (T) -> List<Float>,
        endValues: () -> List<Float>,
        updateValues: (T, List<Float>) -> T,
        duration: Seconds,
        interpolation: Interpolation,
        loop: Boolean = true,
        enabled: Boolean = true,
        reverse: Boolean = false,
        pingpong: Boolean = false
    ): Tween<T> {
        val tween = TweeningExecutor(
            duration = duration,
            interpolation = interpolation,
            seed = seed,
            startValues = startValues,
            endValues = endValues,
            updateValues = updateValues
        ).apply {
            this.loop = loop
            this.enabled = enabled
            this.reverse = reverse
            this.pingpong = pingpong
        }
        generatedTweens.add(tween)
        return tween
    }

    fun vector3(
        start: Vector3,
        end: Vector3,
        duration: Seconds,
        interpolation: Interpolation,
        loop: Boolean = true,
        enabled: Boolean = true,
        reverse: Boolean = false,
        pingpong: Boolean = false,
    ): Tween<Vector3> {
        return tween(
            seed = start,
            startValues = { v3 -> listOf(v3.x, v3.y, v3.z) },
            endValues = { listOf(end.x, end.y, end.z) },
            updateValues = { v3, values ->
                v3.apply {
                    x = values[0]
                    y = values[1]
                    z = values[2]
                }
            },
            duration = duration,
            interpolation = interpolation,
            loop = loop,
            enabled = enabled,
            reverse = reverse,
            pingpong = pingpong
        )
    }

    fun vector2(
        start: Vector2,
        end: Vector2,
        duration: Seconds,
        interpolation: Interpolation,
        loop: Boolean = true,
        enabled: Boolean = true,
        reverse: Boolean = false,
        pingpong: Boolean = false,
    ): Tween<Vector2> {
        return tween(
            seed = start,
            startValues = { v2 -> listOf(v2.x, v2.y) },
            endValues = { listOf(end.x, end.y) },
            updateValues = { v2, values ->
                v2.apply {
                    x = values[0]
                    y = values[1]
                }
            },
            duration = duration,
            interpolation = interpolation,
            loop = loop,
            enabled = enabled,
            reverse = reverse,
            pingpong = pingpong
        )
    }

    fun float(
        start: Float,
        end: Float,
        duration: Seconds,
        interpolation: Interpolation,
        loop: Boolean = true,
        enabled: Boolean = true,
        reverse: Boolean = false,
        pingpong: Boolean = false,
    ): Tween<Float> {
        return tween(
            seed = start,
            startValues = { f -> listOf(f) },
            endValues = { listOf(end) },
            updateValues = { f, values ->
                values.first()
            },
            duration = duration,
            interpolation = interpolation,
            loop = loop,
            enabled = enabled,
            reverse = reverse,
            pingpong = pingpong
        )
    }
}

interface Tween<T> {

    fun reset(): Tween<T>

    var reverse: Boolean

    var loop: Boolean

    // When finished, reverse.
    var pingpong: Boolean

    var enabled: Boolean

    var interpolation: Interpolation

    val current: TweenResult<T>

    fun update(delta: Seconds): TweenResult<T>
}

class TweenResult<T>(value: T) {
    var value: T = value
        internal set
    var percent: Float = 0f
        internal set
}
private class TweeningExecutor<T>(
    private val duration: Seconds,
    override var interpolation: Interpolation,
    private val seed: T,
    private val startValues: (T) -> List<Float>,
    private val endValues: () -> List<Float>,
    private val updateValues: (T, List<Float>) -> T,
) : Tween<T> {

    private var elapsedDuration: Seconds = 0f

    override var reverse = false
    override var loop: Boolean = true
    override var pingpong: Boolean = true
    override var enabled: Boolean = true

    private val computedStartValues = startValues(seed)

    override val current = TweenResult(seed)

    override fun update(delta: Seconds): TweenResult<T> {
        elapsedDuration += delta
        if (elapsedDuration > duration) {
            elapsedDuration = duration
        }
        val percent = elapsedDuration / duration
        val progress = if (!reverse) {
            percent
        } else {
            1f - percent
        }
        val end = endValues()

        val current = computedStartValues.mapIndexed { index, v -> interpolation.interpolate(v, end[index], progress) }
        val seedUpdated = updateValues(seed, current)

        if (percent >= 1.0 && loop) {
            elapsedDuration = 0f
        }
        if (percent >= 1.0 && pingpong) {
            reverse = !reverse
        }

        this.current.percent = percent
        this.current.value = seedUpdated
        return this.current
    }

    override fun reset(): Tween<T> {
        elapsedDuration = 0f
        updateValues(seed, computedStartValues)
        return this
    }
}

class Moveable private constructor(
    private val entity: Entity,
    start: ImmutableVector3,
    private val target: ImmutableVector3,
    duration: Seconds,
    interpolation: Interpolation = Interpolations.linear
) {

    constructor(
        entity: Entity,
        target: Entity,
        duration: Seconds,
        interpolation: Interpolation = Interpolations.linear,
    ) : this(
        entity,
        entity.position.translation,
        target.position.translation,
        duration,
        interpolation
    )

    constructor(
        entity: Entity,
        target: ImmutableVector3,
        duration: Seconds,
        interpolation: Interpolation = Interpolations.linear,
    ) : this(
        entity,
        entity.position.translation,
        target,
        duration,
        interpolation
    )

    private val tween: Tween<Vector3>

    private val mutableStart = start.mutable()

    init {
        tween = TweeningExecutor(
            duration = duration,
            interpolation = interpolation,
            seed = mutableStart,
            startValues = { v3 -> listOf(v3.x, v3.y, v3.z) },
            endValues = { listOf(target.x, target.y, target.z) },
            updateValues = { v3, values ->
                v3.apply {
                    x = values[0]
                    y = values[1]
                    z = values[2]
                }
            },

        )
    }

    fun update(delta: Seconds): Boolean {
        val result = tween.update(delta)

        entity.position.setGlobalTranslation(mutableStart)

        return (result.percent >= 1.0f)
    }
}
