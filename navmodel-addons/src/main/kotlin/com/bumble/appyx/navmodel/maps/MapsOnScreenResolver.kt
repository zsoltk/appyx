package com.bumble.appyx.navmodel.maps

import com.bumble.appyx.navmodel.maps.Maps.State
import com.bumble.appyx.core.navigation.onscreen.OnScreenStateResolver

object MapsOnScreenResolver : OnScreenStateResolver<State> {
    override fun isOnScreen(state: State): Boolean =
        when (state) {
            State.DESTROYED -> false
            else -> true
        }
}
