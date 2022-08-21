package com.bumble.appyx.navmodel.maps.backpresshandler

import com.bumble.appyx.core.navigation.backpresshandlerstrategies.BaseBackPressHandlerStrategy
import com.bumble.appyx.navmodel.maps.Maps.State
import com.bumble.appyx.navmodel.maps.Maps.State.DEFAULT
import com.bumble.appyx.navmodel.maps.operation.DefaultMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RevertBackPressHandler<Routing : Any> :
    BaseBackPressHandlerStrategy<Routing, State>() {

    override val canHandleBackPressFlow: Flow<Boolean> by lazy {
        navModel.elements.map { it.any { it.targetState != DEFAULT } }
    }

    override fun onBackPressed() {
        navModel.accept(DefaultMode())
    }
}
