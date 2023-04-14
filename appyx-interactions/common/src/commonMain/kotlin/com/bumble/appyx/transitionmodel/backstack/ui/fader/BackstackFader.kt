package com.bumble.appyx.transitionmodel.backstack.ui.fader

import DefaultAnimationSpec
import androidx.compose.animation.core.SpringSpec
import com.bumble.appyx.interactions.core.ui.context.UiContext
import com.bumble.appyx.interactions.core.ui.property.impl.Alpha
import com.bumble.appyx.interactions.core.ui.state.UiMapping
import com.bumble.appyx.transitionmodel.BaseMotionController
import com.bumble.appyx.transitionmodel.backstack.BackStackModel

class BackstackFader<InteractionTarget : Any>(
    uiContext: UiContext,
    defaultAnimationSpec: SpringSpec<Float> = DefaultAnimationSpec
) : BaseMotionController<InteractionTarget, BackStackModel.State<InteractionTarget>, MutableUiState, TargetUiState>(
    uiContext = uiContext,
    defaultAnimationSpec = defaultAnimationSpec,
) {
    private val visible = TargetUiState(
        alpha = Alpha.Target( 1f)
    )

    private val hidden = TargetUiState(
        alpha = Alpha.Target( 0f)
    )

    override fun BackStackModel.State<InteractionTarget>.toUiTargets(): List<UiMapping<InteractionTarget, TargetUiState>> =
        listOf(
            UiMapping(active, visible)
        ) + (created + stashed + destroyed).map {
            UiMapping(it, hidden)
        }

    override fun mutableUiStateFor(uiContext: UiContext, uiMapping: UiMapping<*, TargetUiState>): MutableUiState =
        uiMapping.targetUiState.toMutableState(uiContext)
}
