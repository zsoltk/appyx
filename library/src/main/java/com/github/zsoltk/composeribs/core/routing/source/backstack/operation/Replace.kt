package com.github.zsoltk.composeribs.core.routing.source.backstack.operation

import com.github.zsoltk.composeribs.core.routing.RoutingKey
import com.github.zsoltk.composeribs.core.routing.source.backstack.BackStack
import com.github.zsoltk.composeribs.core.routing.source.backstack.BackStackElement
import com.github.zsoltk.composeribs.core.routing.source.backstack.BackStackElements
import com.github.zsoltk.composeribs.core.routing.source.backstack.BackStackOnScreenResolver
import com.github.zsoltk.composeribs.core.routing.source.backstack.current
import com.github.zsoltk.composeribs.core.routing.source.backstack.currentIndex

/**
 * Operation:
 *
 * [A, B, C] + Replace(D) = [A, B, D]
 */
internal class Replace<T : Any>(
    private val element: T
) : BackStack.Operation<T> {

    override fun isApplicable(elements: BackStackElements<T>): Boolean =
        element != elements.current?.key?.routing

    override fun invoke(
        elements: BackStackElements<T>
    ): BackStackElements<T> {
        require(elements.any { it.targetState == BackStack.TransitionState.ON_SCREEN }) { "No element to be replaced, state=$elements" }

        return elements.mapIndexed { index, element ->
            if (index == elements.currentIndex) {
                element.transitionTo(targetState = BackStack.TransitionState.DESTROYED)
            } else {
                element
            }
        } + BackStackElement(
            onScreenResolver = BackStackOnScreenResolver,
            key = RoutingKey(element),
            fromState = BackStack.TransitionState.CREATED,
            targetState = BackStack.TransitionState.ON_SCREEN,
            onScreen = true
        )
    }
}

fun <T : Any> BackStack<T>.replace(element: T) {
    perform(Replace(element))
}
