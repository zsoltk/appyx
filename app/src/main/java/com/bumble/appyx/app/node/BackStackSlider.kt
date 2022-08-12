package com.bumble.appyx.app.node

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.bumble.appyx.app.ui.md_amber_900
import com.bumble.appyx.app.ui.md_blue_grey_900
import com.bumble.appyx.app.ui.md_cyan_500
import com.bumble.appyx.app.ui.md_cyan_700
import com.bumble.appyx.app.ui.md_teal_700
import com.bumble.appyx.routingsource.backstack.BackStack
import com.bumble.appyx.routingsource.backstack.operation.BackStackOperation
import com.bumble.appyx.routingsource.backstack.operation.NewRoot
import com.bumble.appyx.routingsource.backstack.operation.Pop
import com.bumble.appyx.routingsource.backstack.operation.Push
import com.bumble.appyx.routingsource.backstack.operation.Remove
import com.bumble.appyx.routingsource.backstack.operation.Replace
import com.bumble.appyx.routingsource.backstack.operation.SingleTop.SingleTopReactivateBackStackOperation
import com.bumble.appyx.routingsource.backstack.operation.SingleTop.SingleTopReplaceBackStackOperation
import com.bumble.appyx.core.routing.transition.ModifierTransitionHandler
import com.bumble.appyx.core.routing.transition.TransitionDescriptor
import com.bumble.appyx.core.routing.transition.TransitionSpec
import kotlin.math.roundToInt

@Suppress("TransitionPropertiesLabel")
class BackStackSlider<T>(
    private val transitionSpec: TransitionSpec<BackStack.TransitionState, Offset> = {
        spring(stiffness = Spring.StiffnessVeryLow)
    },
    override val clipToBounds: Boolean = false
) : ModifierTransitionHandler<T, BackStack.TransitionState>() {

    override fun createModifier(
        modifier: Modifier,
        transition: Transition<BackStack.TransitionState>,
        descriptor: TransitionDescriptor<T, BackStack.TransitionState>
    ): Modifier = modifier.composed {
        val offset = transition.animateOffset(
            transitionSpec = { spring(stiffness = 30f) },
            targetValueByState = {
                val width = descriptor.params.bounds.width.value
                when (it) {
                    BackStack.TransitionState.CREATED -> toOutsideRight(width)
                    BackStack.TransitionState.ACTIVE -> toCenter()
                    BackStack.TransitionState.STASHED_IN_BACK_STACK -> toOutsideLeft(width)
                    BackStack.TransitionState.DESTROYED -> toOutsideRightDouble(width)
                }
            })

        val color by transition.animateColor(
            transitionSpec = { spring(stiffness = 30f) },
            label = "",
            targetValueByState = {
                when (it) {
                    BackStack.TransitionState.CREATED -> md_cyan_500
                    BackStack.TransitionState.ACTIVE -> md_blue_grey_900
                    BackStack.TransitionState.STASHED_IN_BACK_STACK -> md_amber_900
                    BackStack.TransitionState.DESTROYED -> md_cyan_700
                }
            }
        )

        val rotation by transition.animateFloat(
            transitionSpec = { spring(stiffness = 30f) },
            label = "",
            targetValueByState = {
                when (it) {
                    BackStack.TransitionState.CREATED -> 0f
                    BackStack.TransitionState.ACTIVE -> 0f
                    BackStack.TransitionState.STASHED_IN_BACK_STACK -> 0f
                    BackStack.TransitionState.DESTROYED -> 90f
                }
            }
        )

        Modifier
            .offset {
                IntOffset(
                    x = (offset.value.x * this.density).roundToInt(),
                    y = (offset.value.y * this.density).roundToInt()
                )
            }
            .rotate(rotation)
            .background(color = color)
    }

    private fun toOutsideRight(width: Float) = Offset(1.0f * width, 0f)
    private fun toOutsideRightDouble(width: Float) = Offset(2.0f * width, 0f)

    private fun toOutsideLeft(width: Float) = Offset(-1.0f * width, 0f)

    private fun toCenter() = Offset(0f, 0f)
}

@Composable
fun <T> rememberBackstackSlider(
    transitionSpec: TransitionSpec<BackStack.TransitionState, Offset> = { spring(stiffness = Spring.StiffnessLow) },
    clipToBounds: Boolean = false
): ModifierTransitionHandler<T, BackStack.TransitionState> = remember {
    BackStackSlider(transitionSpec = transitionSpec, clipToBounds = clipToBounds)
}
