package com.bumble.appyx.navmodel2.backstack.operation

import com.bumble.appyx.core.navigation.NavKey
import com.bumble.appyx.navmodel2.backstack.BackStack
import com.bumble.appyx.navmodel2.backstack.BackStackElement
import com.bumble.appyx.navmodel2.backstack.BackStackElements
import com.bumble.appyx.navmodel2.backstack.BackStack.State.ACTIVE
import com.bumble.appyx.navmodel2.backstack.BackStack.State.CREATED
import com.bumble.appyx.navmodel2.backstack.BackStack.State.STASHED
import com.bumble.appyx.navmodel2.backstack.activeElement
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Operation:
 *
 * [A, B, C] + Push(D) = [A, B, C, D]
 */
@Parcelize
data class Push<T : Any>(
    private val element: @RawValue T
) : BackStackOperation<T> {

    override fun isApplicable(elements: BackStackElements<T>): Boolean =
        element != elements.activeElement

    override fun invoke(elements: BackStackElements<T>): BackStackElements<T> =
        elements.transitionTo(STASHED) {
            it.targetState == ACTIVE
        } + BackStackElement(
            key = NavKey(element),
            fromState = CREATED,
            targetState = ACTIVE,
            operation = this
        )
}

fun <T : Any> BackStack<T>.push(element: T) {
    enqueue(Push(element))
}