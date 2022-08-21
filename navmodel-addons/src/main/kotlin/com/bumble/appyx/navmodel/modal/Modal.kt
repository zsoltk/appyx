package com.bumble.appyx.navmodel.modal

import com.bumble.appyx.core.navigation.BaseNavModel
import com.bumble.appyx.core.navigation.Operation.Noop
import com.bumble.appyx.core.navigation.RoutingElements
import com.bumble.appyx.core.navigation.RoutingKey
import com.bumble.appyx.core.navigation.backpresshandlerstrategies.BackPressHandlerStrategy
import com.bumble.appyx.core.navigation.onscreen.OnScreenStateResolver
import com.bumble.appyx.core.navigation.operationstrategies.ExecuteImmediately
import com.bumble.appyx.core.navigation.operationstrategies.OperationStrategy
import com.bumble.appyx.core.state.SavedStateMap
import com.bumble.appyx.navmodel.modal.Modal.TransitionState
import com.bumble.appyx.navmodel.modal.Modal.TransitionState.CREATED
import com.bumble.appyx.navmodel.modal.Modal.TransitionState.DESTROYED
import com.bumble.appyx.navmodel.modal.backpresshandler.RevertBackPressHandler

class Modal<Routing : Any>(
    initialElement: Routing,
    savedStateMap: SavedStateMap?,
    key: String = KEY_NAV_MODEL,
    backPressHandler: BackPressHandlerStrategy<Routing, TransitionState> = RevertBackPressHandler(),
    operationStrategy: OperationStrategy<Routing, TransitionState> = ExecuteImmediately(),
    screenResolver: OnScreenStateResolver<TransitionState> = ModalOnScreenResolver
) : BaseNavModel<Routing, TransitionState>(
    savedStateMap = savedStateMap,
    screenResolver = screenResolver,
    operationStrategy = operationStrategy,
    backPressHandler = backPressHandler,
    key = key,
    finalState = DESTROYED
) {

    enum class TransitionState {
        CREATED, MODAL, FULL_SCREEN, DESTROYED
    }

    override val initialElements: RoutingElements<Routing, TransitionState> = listOf(
        ModalElement(
            key = RoutingKey(initialElement),
            fromState = CREATED,
            targetState = CREATED,
            operation = Noop()
        )
    )
}
