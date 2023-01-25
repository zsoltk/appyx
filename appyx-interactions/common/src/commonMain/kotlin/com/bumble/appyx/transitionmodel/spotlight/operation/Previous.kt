package com.bumble.appyx.transitionmodel.spotlight.operation

import androidx.compose.animation.core.AnimationSpec
import com.bumble.appyx.interactions.Parcelize
import com.bumble.appyx.interactions.core.BaseOperation
import com.bumble.appyx.interactions.core.TransitionModel
import com.bumble.appyx.interactions.core.TransitionModel.OperationMode
import com.bumble.appyx.interactions.core.TransitionModel.OperationMode.UPDATE
import com.bumble.appyx.transitionmodel.spotlight.Spotlight
import com.bumble.appyx.transitionmodel.spotlight.SpotlightModel


@Parcelize
class Previous<NavTarget> : BaseOperation<SpotlightModel.State<NavTarget>>() {

    override fun isApplicable(state: SpotlightModel.State<NavTarget>): Boolean =
        state.hasPrevious()

    override fun createFromState(baseLineState: SpotlightModel.State<NavTarget>): SpotlightModel.State<NavTarget> =
        baseLineState

    override fun createTargetState(fromState: SpotlightModel.State<NavTarget>): SpotlightModel.State<NavTarget> =
        fromState.copy(
            activeIndex = fromState.activeIndex - 1f,
        )
}

fun <NavTarget : Any> Spotlight<NavTarget>.previous(
    animationSpec: AnimationSpec<Float> = defaultAnimationSpec,
    mode: OperationMode = UPDATE,
) {
    operation(Previous(), animationSpec, mode)
}
