package com.bumble.appyx.navmodel.maps.operation

import com.bumble.appyx.navmodel.maps.Maps
import com.bumble.appyx.navmodel.maps.MapsElements
import kotlinx.parcelize.Parcelize

@Parcelize
object VenueListMode : MapsOperation {

    override fun isApplicable(elements: MapsElements): Boolean =
        true

    override fun invoke(elements: MapsElements): MapsElements {
        return elements.map {
            it.transitionTo(
                newTargetState = Maps.State.VENUES_LIST,
                operation = this
            )
        }
    }
}

fun Maps.venueListMode() {
    accept(VenueListMode)
}
