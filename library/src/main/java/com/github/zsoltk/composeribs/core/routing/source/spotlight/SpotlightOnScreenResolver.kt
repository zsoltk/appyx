package com.github.zsoltk.composeribs.core.routing.source.spotlight

import com.github.zsoltk.composeribs.core.routing.OnScreenStateResolver

object SpotlightOnScreenResolver : OnScreenStateResolver<Spotlight.TransitionState> {
    override fun isOnScreen(state: Spotlight.TransitionState): Boolean =
        when (state) {
            Spotlight.TransitionState.INACTIVE_BEFORE,
            Spotlight.TransitionState.INACTIVE_AFTER -> false
            Spotlight.TransitionState.ACTIVE -> true
        }
}