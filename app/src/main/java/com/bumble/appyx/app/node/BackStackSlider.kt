package com.bumble.appyx.app.node

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import com.bumble.appyx.app.ui.md_light_blue_500
import com.bumble.appyx.app.ui.md_light_green_500
import com.bumble.appyx.app.ui.md_purple_500
import com.bumble.appyx.app.ui.md_red_700
import com.bumble.appyx.core.routing.transition.ModifierTransitionHandler
import com.bumble.appyx.core.routing.transition.TransitionDescriptor
import com.bumble.appyx.core.routing.transition.TransitionSpec
import com.bumble.appyx.routingsource.backstack.BackStack
import kotlin.math.roundToInt

@Suppress("TransitionPropertiesLabel")
class BackStackSlider<T>(
    private val transitionSpec: TransitionSpec<BackStack.TransitionState, Offset> = {
        spring(stiffness = Spring.StiffnessLow / 8)
    },
    override val clipToBounds: Boolean = false
) : ModifierTransitionHandler<T, BackStack.TransitionState>() {

    override fun createModifier(
        modifier: Modifier,
        transition: Transition<BackStack.TransitionState>,
        descriptor: TransitionDescriptor<T, BackStack.TransitionState>
    ): Modifier = modifier.composed {
        val offset = transition.animateOffset(
            transitionSpec = { spring(stiffness = Spring.StiffnessLow / 8) },
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
            transitionSpec = { spring(stiffness = Spring.StiffnessLow / 8) },
            label = "",
            targetValueByState = {
                when (it) {
                    BackStack.TransitionState.CREATED -> md_purple_500
                    BackStack.TransitionState.ACTIVE -> md_light_blue_500
                    BackStack.TransitionState.STASHED_IN_BACK_STACK -> md_light_green_500
                    BackStack.TransitionState.DESTROYED -> md_red_700
                }
            }
        )

        val rotation by transition.animateFloat(
            transitionSpec = { spring(stiffness = Spring.StiffnessLow / 8) },
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
    transitionSpec: TransitionSpec<BackStack.TransitionState, Offset> = {
        spring(stiffness = Spring.StiffnessLow / 8)
    },
    clipToBounds: Boolean = false
): ModifierTransitionHandler<T, BackStack.TransitionState> = remember {
    BackStackSlider(transitionSpec = transitionSpec, clipToBounds = clipToBounds)
}
