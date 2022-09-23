package com.github.dwursteisen.minigdx.math

infix fun Number.v2(other: Number) = Vector2(this, other)

fun Array<Vector2>.asFloatArray(): FloatArray {
    return this.flatMap { listOf(it.x, it.y) }
        .toFloatArray()
}

data class Vector2(var x: Float, var y: Float) {

    constructor(x: Number, y: Number) : this(x.toFloat(), y.toFloat())

    fun set(other: Vector2): Vector2 {
        this.x = other.x
        this.y = other.y
        return this
    }
}
