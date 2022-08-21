package com.bumble.appyx.navmodel.maps.backpresshandler

import com.bumble.appyx.core.navigation.backpresshandlerstrategies.BaseBackPressHandlerStrategy
import com.bumble.appyx.navmodel.maps.Maps.State
import com.bumble.appyx.navmodel.maps.Maps.State.FULL_SCREEN
import com.bumble.appyx.navmodel.maps.MapsElements
import com.bumble.appyx.navmodel.maps.operation.Revert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RevertBackPressHandler<Routing : Any> :
    BaseBackPressHandlerStrategy<Routing, State>() {

    override val canHandleBackPressFlow: Flow<Boolean> by lazy {
        navModel.elements.map(::areThereFullScreenElements)
    }

    private fun areThereFullScreenElements(elements: MapsElements<Routing>) =
        elements.any { it.targetState == FULL_SCREEN }

    override fun onBackPressed() {
        navModel.accept(Revert())
    }
}
