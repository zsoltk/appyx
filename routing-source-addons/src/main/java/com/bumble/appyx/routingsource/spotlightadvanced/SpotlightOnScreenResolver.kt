package com.bumble.appyx.routingsource.spotlight

import com.bumble.appyx.core.routing.onscreen.OnScreenStateResolver
import com.bumble.appyx.routingsource.spotlightadvanced.Spotlight.TransitionState
import com.bumble.appyx.routingsource.spotlightadvanced.Spotlight.TransitionState.ACTIVE
import com.bumble.appyx.routingsource.spotlightadvanced.Spotlight.TransitionState.INACTIVE_AFTER
import com.bumble.appyx.routingsource.spotlightadvanced.Spotlight.TransitionState.INACTIVE_BEFORE

object SpotlightOnScreenResolver : OnScreenStateResolver<TransitionState> {
    override fun isOnScreen(state: TransitionState): Boolean =
        when (state) {
            INACTIVE_BEFORE,
            INACTIVE_AFTER -> false
            ACTIVE -> true
        }
}
