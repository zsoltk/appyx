package com.bumble.appyx.routingsource.spotlightadvanced

import com.bumble.appyx.core.routing.onscreen.OnScreenStateResolver
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.ACTIVE
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.INACTIVE_AFTER
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.INACTIVE_BEFORE

object SpotlightAdvancedOnScreenResolver : OnScreenStateResolver<TransitionState> {
    override fun isOnScreen(state: TransitionState): Boolean =
        when (state) {
            INACTIVE_BEFORE,
            INACTIVE_AFTER -> false
            ACTIVE -> true
        }
}
