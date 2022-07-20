package com.bumble.appyx.routingsource.spotlightadvanced.operation

import com.bumble.appyx.core.routing.RoutingElements
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.ACTIVE
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.INACTIVE_AFTER
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.INACTIVE_BEFORE
import kotlinx.parcelize.Parcelize


@Parcelize
class Previous<T : Any> : SpotlightAdvancedOperation<T> {

    override fun isApplicable(elements: RoutingElements<T, SpotlightAdvanced.TransitionState>) =
        elements.any { it.fromState == INACTIVE_BEFORE }

    override fun invoke(elements: RoutingElements<T, SpotlightAdvanced.TransitionState>): RoutingElements<T, SpotlightAdvanced.TransitionState> {
        val previousKey =
            elements.last { it.targetState == INACTIVE_BEFORE }.key

        return elements.map {
            when {
                it.targetState == ACTIVE -> {
                    it.transitionTo(
                        newTargetState = INACTIVE_AFTER,
                        operation = this
                    )
                }
                it.key == previousKey -> {
                    it.transitionTo(
                        newTargetState = ACTIVE,
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


