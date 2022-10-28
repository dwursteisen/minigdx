package com.github.dwursteisen.minigdx.ecs.components

import com.github.dwursteisen.minigdx.ecs.entities.Entity
import kotlin.reflect.KClass

interface Component {

    /**
     * Call when the component is added from [entity].
     */
    fun onAdded(entity: Entity) = Unit

    /**
     * Call when the component is removed from the [entity].
     */
    fun onRemoved(entity: Entity) = Unit

    /**
     * Call when the component of type [componentType] from the current entity is updated.
     */
    fun onComponentUpdated(componentType: KClass<out Component>) = Unit

    /**
     * Call when the entity is detach from it's parent.
     */
    fun onDetach(parent: Entity) = Unit

    /**
     * Call when the entity is attach to it's parent.
     */
    fun onAttach(parent: Entity) = Unit
}
