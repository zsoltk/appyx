package com.bumble.appyx.navmodel.maps.operation

import com.bumble.appyx.navmodel.maps.Maps
import com.bumble.appyx.navmodel.maps.MapsElements
import kotlinx.parcelize.Parcelize

@Parcelize
class VenueShowMode<T : Any> : MapsOperation<T> {

    override fun isApplicable(elements: MapsElements<T>): Boolean =
        true

    override fun invoke(elements: MapsElements<T>): MapsElements<T> {
        return elements.map {
                it.transitionTo(
                    newTargetState = Maps.State.VENUE_SHOW,
                    operation = this
                )
        }
    }
}

fun <T : Any> Maps<T>.venueShowMode() {
    accept(VenueShowMode())
}
