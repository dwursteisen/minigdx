package com.github.dwursteisen.minigdx.ecs.systems

import com.github.dwursteisen.minigdx.Seconds
import com.github.dwursteisen.minigdx.ecs.components.position.TweenFactoryComponent
import com.github.dwursteisen.minigdx.ecs.entities.Entity

class TweenSystem : System(EntityQuery.of(TweenFactoryComponent::class)) {

    override fun update(delta: Seconds, entity: Entity) {
        entity.findAll(TweenFactoryComponent::class).forEach { component ->
            component.generatedTweens.filter { it.enabled }
                .forEach { it.update(delta) }
        }
    }
}
