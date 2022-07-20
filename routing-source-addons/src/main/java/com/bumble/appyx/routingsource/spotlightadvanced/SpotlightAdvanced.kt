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
    init {
//        if (items.size != 4) {
//            error("This is just a demonstration example, tailored to exactly 4 elements." +
//                "Feel free to adapt this routing source to support an arbitrary number elements.")
//        }
    }

    sealed class TransitionState {
        object InactiveBefore : TransitionState()
        object Active : TransitionState()
        object InactiveAfter : TransitionState()
        data class Circular(val offset: Int, val max: Int) : TransitionState()
    }

    override val initialElements = items.toSpotlightAdvancedElements(initialActiveIndex)

}
