package com.bumble.appyx.helpers

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import com.bumble.appyx.helpers.DummyComponentModel.State
import com.bumble.appyx.interactions.gesture.GestureFactory
import com.bumble.appyx.interactions.gesture.GestureSettleConfig
import com.bumble.appyx.interactions.model.BaseAppyxComponent
import com.bumble.appyx.interactions.model.backpresshandlerstrategies.BackPressHandlerStrategy
import com.bumble.appyx.interactions.model.backpresshandlerstrategies.DontHandleBackPress
import com.bumble.appyx.interactions.state.MutableSavedStateMap
import com.bumble.appyx.interactions.ui.Visualisation
import com.bumble.appyx.interactions.ui.context.TransitionBounds
import com.bumble.appyx.interactions.ui.context.UiContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class DummyComponent<NavTarget : Any>(
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
    val model: DummyComponentModel<NavTarget>,
    visualisation: (UiContext) -> Visualisation<NavTarget, State<NavTarget>>,
    animationSpec: AnimationSpec<Float> = spring(),
    gestureFactory: (TransitionBounds) -> GestureFactory<NavTarget, State<NavTarget>> = {
        GestureFactory.Noop()
    },
    gestureSettleConfig: GestureSettleConfig = GestureSettleConfig(),
    backPressStrategy: BackPressHandlerStrategy<NavTarget, State<NavTarget>> = DontHandleBackPress(),
    disableAnimations: Boolean = false,
) : BaseAppyxComponent<NavTarget, State<NavTarget>>(
    scope = scope,
    model = model,
    visualisation = visualisation,
    gestureFactory = gestureFactory,
    gestureSettleConfig = gestureSettleConfig,
    backPressStrategy = backPressStrategy,
    defaultAnimationSpec = animationSpec,
    disableAnimations = disableAnimations
) {
    var saveInstanceStateInvoked: Int = 0

    fun resetSaveInstanceState() {
        saveInstanceStateInvoked = 0
    }

    override fun saveInstanceState(state: MutableSavedStateMap) {
        super.saveInstanceState(state)
        saveInstanceStateInvoked += 1
    }
}
