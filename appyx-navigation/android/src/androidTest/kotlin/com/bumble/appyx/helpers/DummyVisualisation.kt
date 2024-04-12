package com.bumble.appyx.helpers

import androidx.compose.animation.core.SpringSpec
import com.bumble.appyx.helpers.DummyComponentModel.State
import com.bumble.appyx.interactions.ui.DefaultAnimationSpec
import com.bumble.appyx.interactions.ui.context.UiContext
import com.bumble.appyx.interactions.ui.state.MatchedTargetUiState
import com.bumble.appyx.transitionmodel.BaseVisualisation

class DummyVisualisation<NavTarget : Any>(
    uiContext: UiContext,
    defaultAnimationSpec: SpringSpec<Float> = DefaultAnimationSpec
) : BaseVisualisation<NavTarget, State<NavTarget>, TargetUiState, MutableUiState>(
    uiContext = uiContext,
    defaultAnimationSpec = defaultAnimationSpec,
) {
    override fun State<NavTarget>.toUiTargets(): List<MatchedTargetUiState<NavTarget, TargetUiState>> =
        listOf(
            MatchedTargetUiState(
                element = target,
                targetUiState = TargetUiState(0)
            )
        )

    override fun mutableUiStateFor(
        uiContext: UiContext,
        targetUiState: TargetUiState
    ): MutableUiState =
        targetUiState.toMutableUiState(uiContext)


}
