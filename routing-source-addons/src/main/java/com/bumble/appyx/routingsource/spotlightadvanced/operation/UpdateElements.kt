package com.bumble.appyx.routingsource.spotlightadvanced.operation

import com.bumble.appyx.core.routing.Operation.Noop
import com.bumble.appyx.core.routing.RoutingElements
import com.bumble.appyx.core.routing.RoutingKey
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.ACTIVE
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.INACTIVE_AFTER
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.INACTIVE_BEFORE
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvancedElement
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvancedElements
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class UpdateElements<T : Any>(
    private val elements: @RawValue List<T>,
    private val initialActiveIndex: Int? = null,
) : SpotlightAdvancedOperation<T> {

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
                this.elements.toSpotlightAdvancedElements(elements.indexOf(currentActiveElement))
            } else {
                // if current routing does not exist in the new list of items and initialActiveIndex is null
                // then set 0 as active index
                this.elements.toSpotlightAdvancedElements(0)
            }
        } else {
            this.elements.toSpotlightAdvancedElements(initialActiveIndex)
        }
    }
}

fun <T : Any> SpotlightAdvanced<T>.updateElements(
    items: List<T>,
    initialActiveItem: Int? = null
) {
    accept(UpdateElements(items, initialActiveItem))
}

internal fun <T> List<T>.toSpotlightAdvancedElements(activeIndex: Int): SpotlightAdvancedElements<T> =
    mapIndexed { index, item ->
        val state = when {
            index < activeIndex -> INACTIVE_BEFORE
            index == activeIndex -> ACTIVE
            else -> INACTIVE_AFTER
        }
        SpotlightAdvancedElement(
            key = RoutingKey(item),
            fromState = state,
            targetState = state,
            operation = Noop()
        )
    }
