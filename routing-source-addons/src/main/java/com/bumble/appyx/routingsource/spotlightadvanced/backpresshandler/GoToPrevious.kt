package com.bumble.appyx.routingsource.spotlightadvanced.backpresshandler

import com.bumble.appyx.core.routing.backpresshandlerstrategies.BaseBackPressHandlerStrategy
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvancedElements
import com.bumble.appyx.routingsource.spotlightadvanced.operation.Previous
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GoToPrevious<Routing : Any> :
    BaseBackPressHandlerStrategy<Routing, SpotlightAdvanced.TransitionState>() {

    override val canHandleBackPressFlow: Flow<Boolean> by lazy {
        routingSource.elements.map(::areTherePreviousElements)
    }

    private fun areTherePreviousElements(elements: SpotlightAdvancedElements<Routing>) =
        elements.any { it.targetState == SpotlightAdvanced.TransitionState.InactiveBefore }

    override fun onBackPressed() {
        routingSource.accept(Previous())
    }
}
