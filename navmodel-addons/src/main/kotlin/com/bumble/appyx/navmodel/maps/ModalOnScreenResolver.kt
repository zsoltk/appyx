package com.bumble.appyx.navmodel.maps

import com.bumble.appyx.navmodel.maps.Maps.TransitionState
import com.bumble.appyx.core.navigation.onscreen.OnScreenStateResolver

object ModalOnScreenResolver : OnScreenStateResolver<TransitionState> {
    override fun isOnScreen(state: TransitionState): Boolean =
        when (state) {
            TransitionState.MODAL,
            TransitionState.FULL_SCREEN,
            TransitionState.CREATED -> true
            TransitionState.DESTROYED -> false
        }
}
