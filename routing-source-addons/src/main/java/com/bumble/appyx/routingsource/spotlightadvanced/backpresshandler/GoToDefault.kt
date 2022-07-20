package com.bumble.appyx.routingsource.spotlightadvanced.backpresshandler

import com.bumble.appyx.core.routing.backpresshandlerstrategies.BaseBackPressHandlerStrategy
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.ACTIVE
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvancedElements
import com.bumble.appyx.routingsource.spotlightadvanced.operation.Activate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GoToDefault<Routing : Any>(
    private val defaultElementIndex: Int = 0
) : BaseBackPressHandlerStrategy<Routing, SpotlightAdvanced.TransitionState>() {

    override val canHandleBackPressFlow: Flow<Boolean> by lazy {
        routingSource.elements.map(::defaultElementIsNotActive)
    }

    override fun onBackPressed() {
        routingSource.accept(Activate(defaultElementIndex))
    }

    private fun defaultElementIsNotActive(elements: SpotlightAdvancedElements<Routing>) =
        elements.getOrNull(defaultElementIndex)?.targetState != ACTIVE
}
