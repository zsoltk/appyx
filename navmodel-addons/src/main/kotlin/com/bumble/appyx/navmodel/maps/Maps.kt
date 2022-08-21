package com.bumble.appyx.navmodel.maps

import com.bumble.appyx.core.navigation.BaseNavModel
import com.bumble.appyx.core.navigation.Operation.Noop
import com.bumble.appyx.core.navigation.RoutingElements
import com.bumble.appyx.core.navigation.RoutingKey
import com.bumble.appyx.core.navigation.backpresshandlerstrategies.BackPressHandlerStrategy
import com.bumble.appyx.core.navigation.onscreen.OnScreenStateResolver
import com.bumble.appyx.core.navigation.operationstrategies.ExecuteImmediately
import com.bumble.appyx.core.navigation.operationstrategies.OperationStrategy
import com.bumble.appyx.core.state.SavedStateMap
import com.bumble.appyx.navmodel.maps.Maps.State
import com.bumble.appyx.navmodel.maps.Maps.State.*
import com.bumble.appyx.navmodel.maps.backpresshandler.RevertBackPressHandler

class Maps(
    savedStateMap: SavedStateMap?,
    key: String = KEY_NAV_MODEL,
    backPressHandler: BackPressHandlerStrategy<Maps.Target, State> = RevertBackPressHandler(),
    operationStrategy: OperationStrategy<Maps.Target, State> = ExecuteImmediately(),
    screenResolver: OnScreenStateResolver<State> = MapsOnScreenResolver
) : BaseNavModel<Maps.Target, State>(
    savedStateMap = savedStateMap,
    screenResolver = screenResolver,
    operationStrategy = operationStrategy,
    backPressHandler = backPressHandler,
    key = key,
    finalState = DESTROYED
) {
    enum class Target {
        MAP, SEARCH, FILTERS, CATEGORIES, VENUE_LIST, VENUE_PAGER
    }

    enum class State {
        CREATED,
        DEFAULT,
        VENUES_LIST,
        VENUES_PAGER,
        VENUE_SHOW,
        DESTROYED
    }

    override val initialElements: RoutingElements<Target, State> = Target.values().map {
        MapsElement(
            key = RoutingKey(it),
            fromState = CREATED,
            targetState = DEFAULT,
            operation = Noop()
        )
    }
}
