package com.bumble.appyx.routingsource.spotlightadvanced

import com.bumble.appyx.core.routing.BaseRoutingSource
import com.bumble.appyx.core.routing.backpresshandlerstrategies.BackPressHandlerStrategy
import com.bumble.appyx.core.routing.onscreen.OnScreenStateResolver
import com.bumble.appyx.core.routing.operationstrategies.ExecuteImmediately
import com.bumble.appyx.core.routing.operationstrategies.OperationStrategy
import com.bumble.appyx.core.state.SavedStateMap
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvancedOnScreenResolver
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState
import com.bumble.appyx.routingsource.spotlightadvanced.backpresshandler.GoToDefault
import com.bumble.appyx.routingsource.spotlightadvanced.operation.toSpotlightAdvancedElements

class SpotlightAdvanced<Routing : Any>(
    items: List<Routing>,
    initialActiveIndex: Int = 0,
    savedStateMap: SavedStateMap?,
    key: String = KEY_ROUTING_SOURCE,
    backPressHandler: BackPressHandlerStrategy<Routing, TransitionState> = GoToDefault(
        initialActiveIndex
    ),
    operationStrategy: OperationStrategy<Routing, TransitionState> = ExecuteImmediately(),
    screenResolver: OnScreenStateResolver<TransitionState> = SpotlightAdvancedOnScreenResolver
) : BaseRoutingSource<Routing, TransitionState>(
    backPressHandler = backPressHandler,
    operationStrategy = operationStrategy,
    screenResolver = screenResolver,
    finalState = null,
    savedStateMap = savedStateMap,
    key = key,
) {

    enum class TransitionState {
        INACTIVE_BEFORE, ACTIVE, INACTIVE_AFTER;
    }

    override val initialElements = items.toSpotlightAdvancedElements(initialActiveIndex)

}
