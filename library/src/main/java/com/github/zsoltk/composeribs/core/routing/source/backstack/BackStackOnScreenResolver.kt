package com.github.zsoltk.composeribs.core.routing.source.backstack

import com.github.zsoltk.composeribs.core.routing.OnScreenResolver
import com.github.zsoltk.composeribs.core.routing.source.backstack.BackStack.TransitionState
import kotlinx.parcelize.Parcelize

@Parcelize
internal object BackStackOnScreenResolver : OnScreenResolver<TransitionState> {
    override fun resolve(state: TransitionState): Boolean =
        when (state) {
            TransitionState.CREATED,
            TransitionState.STASHED_IN_BACK_STACK,
            TransitionState.DESTROYED,
            TransitionState.ON_SCREEN -> true
        }
}
