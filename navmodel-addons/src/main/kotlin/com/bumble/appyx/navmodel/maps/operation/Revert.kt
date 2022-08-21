package com.bumble.appyx.navmodel.maps.operation

import com.bumble.appyx.navmodel.maps.Maps
import com.bumble.appyx.navmodel.maps.Maps.TransitionState.FULL_SCREEN
import com.bumble.appyx.navmodel.maps.Maps.TransitionState.MODAL
import com.bumble.appyx.navmodel.maps.MapsElements
import kotlinx.parcelize.Parcelize

@Parcelize
class Revert<T : Any> : MapsOperation<T> {

    override fun isApplicable(elements: MapsElements<T>): Boolean =
        true

    override fun invoke(elements: MapsElements<T>): MapsElements<T> {
        return elements.map {
            when (it.targetState) {
                FULL_SCREEN -> {
                    it.transitionTo(
                        newTargetState = MODAL,
                        operation = this
                    )
                }
                MODAL -> {
                    it.transitionTo(
                        newTargetState = FULL_SCREEN,
                        operation = this
                    )
                }
                else -> {
                    it
                }
            }
        }
    }
}

fun <T : Any> Maps<T>.revert() {
    accept(Revert())
}
