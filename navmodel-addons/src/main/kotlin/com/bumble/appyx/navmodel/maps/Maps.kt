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

class Maps<Routing : Any>(
    controls: Controls<Routing>,
    savedStateMap: SavedStateMap?,
    key: String = KEY_NAV_MODEL,
    backPressHandler: BackPressHandlerStrategy<Routing, State> = RevertBackPressHandler(),
    operationStrategy: OperationStrategy<Routing, State> = ExecuteImmediately(),
    screenResolver: OnScreenStateResolver<State> = ModalOnScreenResolver
) : BaseNavModel<Routing, State>(
    savedStateMap = savedStateMap,
    screenResolver = screenResolver,
    operationStrategy = operationStrategy,
    backPressHandler = backPressHandler,
    key = key,
    finalState = DESTROYED
) {
    data class Controls<Routing>(
        val map: Routing,
        val search: Routing,
        val categories: Routing,
        val venueList: Routing,
        val venueCarousel: Routing,
    )

    enum class State {
        CREATED,
        DEFAULT,
        VENUES_LIST,
        VENUES_PAGER,
        VENUE_SHOW,
        DESTROYED
    }

    override val initialElements: RoutingElements<Routing, State> = listOf(
        controls.map,
        controls.search,
        controls.categories,
        controls.venueList,
        controls.venueCarousel
    ).map {
        MapsElement(
            key = RoutingKey(it),
            fromState = CREATED,
            targetState = DEFAULT,
            operation = Noop()
        )
    }
}
