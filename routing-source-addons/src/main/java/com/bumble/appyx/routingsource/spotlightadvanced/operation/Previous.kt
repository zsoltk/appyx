package com.bumble.appyx.routingsource.spotlightadvanced.operation

import com.bumble.appyx.core.routing.RoutingElements
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.Active
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.InactiveAfter
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.InactiveBefore
import kotlinx.parcelize.Parcelize


@Parcelize
class Previous<T : Any> : SpotlightAdvancedOperation<T> {

    override fun isApplicable(elements: RoutingElements<T, SpotlightAdvanced.TransitionState>) =
        elements.any { it.fromState == InactiveBefore }

    override fun invoke(elements: RoutingElements<T, SpotlightAdvanced.TransitionState>): RoutingElements<T, SpotlightAdvanced.TransitionState> {
        val previousKey =
            elements.last { it.targetState == InactiveBefore }.key

        return elements.map {
            when {
                it.targetState == Active -> {
                    it.transitionTo(
                        newTargetState = InactiveAfter,
                        operation = this
                    )
                }
                it.key == previousKey -> {
                    it.transitionTo(
                        newTargetState = Active,
                        operation = this
                    )
                }
                else -> {
                    it
                }
            }
        }
    }
}

fun <T : Any> SpotlightAdvanced<T>.previous() {
    accept(Previous())
}


