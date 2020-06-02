package com.github.dwursteisen.minigdx.ecs

import kotlin.reflect.KClass

class EntityQuery(
    val include: List<KClass<out Component>>,
    val exclude: List<KClass<out Component>> = emptyList()
) {

    constructor(vararg include: KClass<out Component>) : this(include.toList(), emptyList())

    fun accept(entity: Entity): Boolean {
        if (exclude.isNotEmpty() && entity.componentsType.containsAll(exclude)) {
            return false
        }
        return entity.componentsType.containsAll(include)
    }
}
