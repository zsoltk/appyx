package com.bumble.appyx.routingsource.spotlightadvanced

import com.bumble.appyx.core.routing.onscreen.OnScreenStateResolver
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.Active
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.Circular
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.InactiveAfter
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.InactiveBefore

object SpotlightAdvancedOnScreenResolver : OnScreenStateResolver<TransitionState> {
    override fun isOnScreen(state: TransitionState): Boolean =
        when (state) {
            is InactiveBefore,
            is InactiveAfter -> false
            is Active,
            is Circular -> true
        }
}
