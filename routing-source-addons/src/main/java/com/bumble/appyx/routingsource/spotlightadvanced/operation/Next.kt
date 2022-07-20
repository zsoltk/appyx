package com.bumble.appyx.routingsource.spotlightadvanced.operation

import com.bumble.appyx.core.routing.RoutingElements
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.ACTIVE
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.INACTIVE_AFTER
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.INACTIVE_BEFORE
import kotlinx.parcelize.Parcelize

@Parcelize
class Next<T : Any> : SpotlightAdvancedOperation<T> {

    override fun isApplicable(elements: RoutingElements<T, TransitionState>) =
        elements.any { it.fromState == INACTIVE_AFTER }

    override fun invoke(elements: RoutingElements<T, TransitionState>): RoutingElements<T, TransitionState> {
        val nextKey =
            elements.first { it.targetState == INACTIVE_AFTER }.key

        return elements.map {
            when {
                it.targetState == ACTIVE -> {
                    it.transitionTo(
                        newTargetState = INACTIVE_BEFORE,
                        operation = this
                    )
                }
                it.key == nextKey -> {
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

fun <T : Any> SpotlightAdvanced<T>.next() {
    accept(Next())
}

