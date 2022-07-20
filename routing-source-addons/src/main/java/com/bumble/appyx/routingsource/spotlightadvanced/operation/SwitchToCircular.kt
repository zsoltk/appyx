package com.bumble.appyx.routingsource.spotlightadvanced.operation

import com.bumble.appyx.core.routing.RoutingElements
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState
import kotlinx.parcelize.Parcelize

@Parcelize
class SwitchToCircular<T : Any>: SpotlightAdvancedOperation<T> {

    override fun isApplicable(elements: RoutingElements<T, TransitionState>) =
        true

    override fun invoke(elements: RoutingElements<T, TransitionState>): RoutingElements<T, TransitionState> {
        val activeIndex =
            elements.indexOfFirst { it.targetState == TransitionState.Active }

        return elements.mapIndexed { index, element ->
            var targetOffset = index - activeIndex
            if (targetOffset < 0) targetOffset += elements.size
            element.transitionTo(
                newTargetState = TransitionState.Circular(offset = targetOffset, max = elements.size),
                operation = this
            )
        }
    }
}


fun <T : Any> SpotlightAdvanced<T>.switchToCircular() {
    accept(SwitchToCircular())
}
