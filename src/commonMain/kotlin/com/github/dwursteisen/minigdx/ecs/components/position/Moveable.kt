package com.github.dwursteisen.minigdx.ecs.components.position

import com.github.dwursteisen.minigdx.Seconds
import com.github.dwursteisen.minigdx.ecs.entities.Entity
import com.github.dwursteisen.minigdx.ecs.entities.position
import com.github.dwursteisen.minigdx.math.ImmutableVector3
import com.github.dwursteisen.minigdx.math.Interpolation
import com.github.dwursteisen.minigdx.math.Interpolations
import kotlin.reflect.KMutableProperty0

interface Tween {

    fun reset(): Tween

    var reverse: Boolean

    fun update(delta: Seconds): Float
}

private class TweeningExecutor(
    private val duration: Seconds,
    private val interpolation: Interpolation,
    fields: Array<out Pair<KMutableProperty0<Float>, Float>>,
) : Tween {

    private val fields: List<Triple<KMutableProperty0<Float>, Float, Float>>

    init {
        this.fields = fields.map { (field, endValue) ->
            val startValue = field.get()
            Triple(field, startValue, endValue)
        }
    }

    private var elapsedDuration: Seconds = 0f

    override var reverse = false

    override fun update(delta: Seconds): Float {
        elapsedDuration += delta
        if (elapsedDuration > duration) {
            elapsedDuration = duration
        }
        val percent = elapsedDuration / duration
        val progress = if(!reverse) {
            percent
        } else {
            1f - percent
        }
        fields.forEach { (field, start, end) ->
            val currentValue = interpolation.interpolate(start, end, progress)
            field.set(currentValue)
        }

        return percent
    }

    override fun reset(): Tween {
        elapsedDuration = 0f
        return this
    }
}

class Tweening(private val duration: Seconds, private val interpolation: Interpolation) {

    private var fields: Array<out Pair<KMutableProperty0<Float>, Float>> = emptyArray()

    fun fields(vararg fields: Pair<KMutableProperty0<Float>, Float>): Tweening {
        this.fields = fields
        return this
    }

    fun build(): Tween {
        return TweeningExecutor(duration, interpolation, fields)
    }

    companion object {

        fun elastic(duration: Seconds): Tweening {
            return Tweening(duration, Interpolations.elastic)
        }

        fun linear(duration: Seconds): Tweening {
            return Tweening(duration, Interpolations.linear)
        }

        fun pow(duration: Seconds): Tweening {
            return Tweening(duration, Interpolations.pow)
        }
    }
}

class Moveable private constructor(
    private val entity: Entity,
    private val start: ImmutableVector3,
    private val target: ImmutableVector3,
    private val duration: Seconds,
    val interpolation: Interpolation = Interpolations.linear
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

    private val tween: Tween

    val mutableStart = start.mutable()
    val mutableEnd = target.mutable()

    init {
        tween = Tweening(duration, interpolation)
            .fields(
                mutableStart::x to mutableEnd.x,
                mutableStart::y to mutableEnd.y,
                mutableStart::z to mutableEnd.z
            )
            .build()
    }

    fun update(delta: Seconds): Boolean {
        val percent = tween.update(delta)

        entity.position.setGlobalTranslation(mutableStart)

        return (percent >= 1.0f)
    }
}
