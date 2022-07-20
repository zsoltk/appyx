package com.bumble.appyx.routingsource.spotlight.operation

import com.bumble.appyx.core.routing.Operation.Noop
import com.bumble.appyx.core.routing.RoutingElements
import com.bumble.appyx.core.routing.RoutingKey
import com.bumble.appyx.routingsource.spotlightadvanced.Spotlight
import com.bumble.appyx.routingsource.spotlightadvanced.Spotlight.TransitionState
import com.bumble.appyx.routingsource.spotlightadvanced.Spotlight.TransitionState.ACTIVE
import com.bumble.appyx.routingsource.spotlightadvanced.Spotlight.TransitionState.INACTIVE_AFTER
import com.bumble.appyx.routingsource.spotlightadvanced.Spotlight.TransitionState.INACTIVE_BEFORE
import com.bumble.appyx.routingsource.spotlight.SpotlightElement
import com.bumble.appyx.routingsource.spotlight.SpotlightElements
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class UpdateElements<T : Any>(
    private val elements: @RawValue List<T>,
    private val initialActiveIndex: Int? = null,
) : SpotlightOperation<T> {

    override fun isApplicable(elements: RoutingElements<T, TransitionState>) = true

    override fun invoke(elements: RoutingElements<T, TransitionState>): RoutingElements<T, TransitionState> {
        if (initialActiveIndex != null) {
            require(initialActiveIndex in this.elements.indices) {
                "Initial active index $initialActiveIndex is out of bounds of provided list of items: ${this.elements.indices}"
            }
        }
        return if (initialActiveIndex == null) {
            val currentActiveElement = elements.find { it.targetState == ACTIVE }

            // if current routing exists in the new list of items and initialActiveIndex is null
            // then keep existing routing active
            if (this.elements.contains(currentActiveElement?.key?.routing)) {
                this.elements.toSpotlightElements(elements.indexOf(currentActiveElement))
            } else {
                // if current routing does not exist in the new list of items and initialActiveIndex is null
                // then set 0 as active index
                this.elements.toSpotlightElements(0)
            }
        } else {
            this.elements.toSpotlightElements(initialActiveIndex)
        }
    }
}

fun <T : Any> Spotlight<T>.updateElements(
    items: List<T>,
    initialActiveItem: Int? = null
) {
    accept(UpdateElements(items, initialActiveItem))
}

internal fun <T> List<T>.toSpotlightElements(activeIndex: Int): SpotlightElements<T> =
    mapIndexed { index, item ->
        val state = when {
            index < activeIndex -> INACTIVE_BEFORE
            index == activeIndex -> ACTIVE
            else -> INACTIVE_AFTER
        }
        SpotlightElement(
            key = RoutingKey(item),
            fromState = state,
            targetState = state,
            operation = Noop()
        )
    }
