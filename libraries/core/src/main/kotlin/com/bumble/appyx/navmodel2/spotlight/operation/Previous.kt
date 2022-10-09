package com.bumble.appyx.navmodel2.spotlight.operation

import com.bumble.appyx.core.navigation2.NavElements
import com.bumble.appyx.core.navigation2.NavTransition
import com.bumble.appyx.core.navigation2.Operation
import com.bumble.appyx.navmodel2.spotlight.Spotlight
import com.bumble.appyx.navmodel2.spotlight.Spotlight.State
import com.bumble.appyx.navmodel2.spotlight.Spotlight.State.ACTIVE
import com.bumble.appyx.navmodel2.spotlight.Spotlight.State.INACTIVE_AFTER
import com.bumble.appyx.navmodel2.spotlight.Spotlight.State.INACTIVE_BEFORE
import kotlinx.parcelize.Parcelize


@Parcelize
class Previous<NavTarget : Any> : Operation<NavTarget, State> {

    override fun isApplicable(elements: NavElements<NavTarget, State>) =
        elements.any { it.fromState == INACTIVE_BEFORE && it.state == INACTIVE_BEFORE }

    override fun invoke(elements: NavElements<NavTarget, State>): NavTransition<NavTarget, State> {
        val previousKey = elements.last { it.state == INACTIVE_BEFORE }.key

        val targetState = elements.map {
            when {
                it.state == ACTIVE -> {
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

        return NavTransition(
            fromState = elements,
            targetState = targetState
        )
    }
}

fun <T : Any> Spotlight<T>.previous() {
    enqueue(Previous())
}

