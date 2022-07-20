package com.bumble.appyx.routingsource.spotlightadvanced.operation

import com.bumble.appyx.core.routing.RoutingElements
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.Active
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.Circular
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.InactiveAfter
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.InactiveBefore
import kotlinx.parcelize.Parcelize

@Parcelize
class Next<T : Any> : SpotlightAdvancedOperation<T> {

    override fun isApplicable(elements: RoutingElements<T, TransitionState>) =
        elements.any { it.fromState == InactiveAfter || it.fromState is Circular }

    override fun invoke(elements: RoutingElements<T, TransitionState>): RoutingElements<T, TransitionState> {
        if (elements.all { it.fromState is Circular }) {
            return elements.map {
                when (it.fromState) {
                    is Circular -> {
                        val currentOffset = (it.fromState as Circular).offset
                        var newOffset = currentOffset + 1
                        if (newOffset > elements.size - 1) newOffset = 0
                        it.transitionTo(
                            newTargetState = Circular(offset = newOffset, max = elements.size),
                            operation = this
                        )
                    }
                    else -> {
                        it
                    }
                }

            }
        } else {
            val nextKey =
                elements.first { it.targetState == InactiveAfter }.key

            return elements.map {
                when {
                    it.targetState == Active -> {
                        it.transitionTo(
                            newTargetState = InactiveBefore,
                            operation = this
                        )
                    }
                    it.key == nextKey -> {
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
}

fun <T : Any> SpotlightAdvanced<T>.next() {
    accept(Next())
}

