package com.bumble.appyx.transitionmodel.spotlight.operation

import androidx.compose.animation.core.AnimationSpec
import com.bumble.appyx.interactions.Parcelize
import com.bumble.appyx.interactions.RawValue
import com.bumble.appyx.interactions.core.BaseOperation
import com.bumble.appyx.interactions.core.NavTransition
import com.bumble.appyx.interactions.core.Operation
import com.bumble.appyx.interactions.core.TransitionModel
import com.bumble.appyx.interactions.core.TransitionModel.OperationMode
import com.bumble.appyx.interactions.core.TransitionModel.OperationMode.ENQUEUE
import com.bumble.appyx.interactions.core.asElement
import com.bumble.appyx.interactions.core.asElements
import com.bumble.appyx.transitionmodel.spotlight.Spotlight
import com.bumble.appyx.transitionmodel.spotlight.SpotlightModel
import com.bumble.appyx.transitionmodel.spotlight.SpotlightModel.State.ElementState.*

@Parcelize
// TODO cleanup SpotlightModel.State.positions if a position doesn't contain more elements
class UpdateElements<NavTarget : Any>(
    private val items: @RawValue List<NavTarget>,
    private val initialActiveIndex: Float? = null,
    private val initialActiveWindow: Float? = null,
) : BaseOperation<SpotlightModel.State<NavTarget>>() {

    override fun isApplicable(state: SpotlightModel.State<NavTarget>): Boolean =
        true

    override fun createFromState(baseLineState: SpotlightModel.State<NavTarget>): SpotlightModel.State<NavTarget> =
        baseLineState.copy(
            positions = baseLineState.positions.mapIndexed { index, position ->
                position.copy(
                    elements = position.elements + (items[index].asElement() to CREATED)
                )
            },
        )

    override fun createTargetState(fromState: SpotlightModel.State<NavTarget>): SpotlightModel.State<NavTarget> =
        fromState.copy(
            positions = fromState.positions.map { position ->
                position.copy(
                    elements = position.elements.mapValues { (_, elementState) ->
                        when (elementState) {
                            CREATED -> STANDARD
                            STANDARD -> DESTROYED
                            DESTROYED -> DESTROYED
                        }
                    }
                )
            },
            activeIndex = initialActiveIndex ?: fromState.activeIndex,
            activeWindow = initialActiveWindow ?: fromState.activeWindow,
        )
}

fun <NavTarget : Any> Spotlight<NavTarget>.updateElements(
    items: List<NavTarget>,
    initialActiveIndex: Float? = null,
    initialActiveWindow: Float? = null,
    animationSpec: AnimationSpec<Float> = defaultAnimationSpec,
    mode: OperationMode = ENQUEUE,
) {
    operation(
        operation = UpdateElements(
            items = items,
            initialActiveIndex = initialActiveIndex,
            initialActiveWindow = initialActiveWindow
        ),
        animationSpec = animationSpec,
        mode = mode
    )
}
