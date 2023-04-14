package com.bumble.appyx.transitionmodel.backstack.ui.slider

import androidx.compose.ui.unit.DpOffset
import com.bumble.appyx.interactions.core.ui.context.UiContext
import com.bumble.appyx.interactions.core.ui.property.impl.Alpha
import com.bumble.appyx.interactions.core.ui.property.impl.Position
import com.bumble.appyx.interactions.core.ui.state.UiMapping
import com.bumble.appyx.transitionmodel.BaseMotionController
import com.bumble.appyx.transitionmodel.backstack.BackStackModel

class BackStackSlider<InteractionTarget : Any>(
    uiContext: UiContext,
) : BaseMotionController<InteractionTarget, BackStackModel.State<InteractionTarget>, MutableUiState, TargetUiState>(
    uiContext = uiContext,
) {
    private val width = uiContext.transitionBounds.widthDp

    private val visible: TargetUiState =
        TargetUiState(
            position = Position.Target(DpOffset.Unspecified),
            alpha = Alpha.Target(1f),
        )

    private val fadeOut: TargetUiState =
        TargetUiState(
            position = Position.Target(DpOffset.Unspecified),
            alpha = Alpha.Target(1f),
        )

    override fun BackStackModel.State<InteractionTarget>.toUiTargets(): List<UiMapping<InteractionTarget, TargetUiState>> =
        created.map { UiMapping(it, visible.toOutsideRight(0, width)) } +
            listOf(active).map { UiMapping(it, visible.toNoOffset() ) } +
            stashed.mapIndexed { index, element ->
                UiMapping(
                    element,
                    visible.toOutsideLeft(index + 1, -width)
                )
            } +
            destroyed.mapIndexed { index, element ->
                UiMapping(
                    element,
                    fadeOut.toOutsideRight(index, width)
                )
            }

    override fun mutableUiStateFor(uiContext: UiContext, uiMapping: UiMapping<*, TargetUiState>): MutableUiState =
        uiMapping.targetUiState.toMutableState(uiContext)
}
