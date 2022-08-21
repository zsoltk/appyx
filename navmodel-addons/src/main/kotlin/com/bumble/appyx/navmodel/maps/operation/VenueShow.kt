package com.bumble.appyx.navmodel.maps.operation

import com.bumble.appyx.navmodel.maps.Maps
import com.bumble.appyx.navmodel.maps.MapsElements
import kotlinx.parcelize.Parcelize

@Parcelize
object VenueShowMode : MapsOperation {

    override fun isApplicable(elements: MapsElements): Boolean =
        true

    override fun invoke(elements: MapsElements): MapsElements {
        return elements.map {
            it.transitionTo(
                newTargetState = Maps.State.VENUE_SHOW,
                operation = this
            )
        }
    }
}

fun Maps.venueShowMode() {
    accept(VenueShowMode)
}
