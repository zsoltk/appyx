package com.bumble.appyx.transitionmodel.spotlight.operation

import androidx.compose.animation.core.AnimationSpec
import com.bumble.appyx.interactions.Parcelize
import com.bumble.appyx.interactions.core.BaseOperation
import com.bumble.appyx.interactions.core.Operation
import com.bumble.appyx.transitionmodel.spotlight.Spotlight
import com.bumble.appyx.transitionmodel.spotlight.SpotlightModel


@Parcelize
class Last<NavTarget : Any>(
    override val mode: Operation.Mode = Operation.Mode.IMMEDIATE
) : BaseOperation<SpotlightModel.State<NavTarget>>() {

    override fun isApplicable(state: SpotlightModel.State<NavTarget>): Boolean =
        true

    override fun createFromState(baseLineState: SpotlightModel.State<NavTarget>): SpotlightModel.State<NavTarget> =
        baseLineState

    override fun createTargetState(fromState: SpotlightModel.State<NavTarget>): SpotlightModel.State<NavTarget> =
        fromState.copy(
            activeIndex = fromState.standard.lastIndex.toFloat(),
        )
}

fun <NavTarget : Any> Spotlight<NavTarget>.last(
    animationSpec: AnimationSpec<Float> = defaultAnimationSpec,
    mode: Operation.Mode = Operation.Mode.IMMEDIATE
) {
    operation(Last(mode), animationSpec)
}