package com.bumble.appyx.navmodel.maps

import com.bumble.appyx.navmodel.maps.Maps.State
import com.bumble.appyx.core.navigation.onscreen.OnScreenStateResolver

object ModalOnScreenResolver : OnScreenStateResolver<State> {
    override fun isOnScreen(state: State): Boolean =
        when (state) {
            State.MODAL,
            State.FULL_SCREEN,
            State.CREATED -> true
            State.DESTROYED -> false
        }
}
