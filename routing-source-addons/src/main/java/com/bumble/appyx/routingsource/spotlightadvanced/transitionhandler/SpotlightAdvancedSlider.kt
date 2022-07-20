package com.bumble.appyx.routingsource.spotlightadvanced.transitionhandler

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.routing.transition.ModifierTransitionHandler
import com.bumble.appyx.core.routing.transition.TransitionDescriptor
import com.bumble.appyx.core.routing.transition.TransitionSpec
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Suppress("TransitionPropertiesLabel")
class SpotlightAdvancedSlider<T>(
    private val childSize: Dp,
    private val transitionSpec: TransitionSpec<TransitionState, Offset> = {
        spring(stiffness = Spring.StiffnessVeryLow)
    },
    override val clipToBounds: Boolean = false
) : ModifierTransitionHandler<T, TransitionState>() {

    override fun createModifier(
        modifier: Modifier,
        transition: Transition<TransitionState>,
        descriptor: TransitionDescriptor<T, TransitionState>
    ): Modifier = modifier.composed {
        val offset = transition.animateOffset(
            transitionSpec = transitionSpec,
            targetValueByState = {
                val width = descriptor.params.bounds.width.value
                val offset = when (it) {
                    is InactiveBefore -> toOutsideLeft(width)
                    is Active -> toCenter()
                    is InactiveAfter -> toOutsideRight(width)
                    is Circular -> {
                        val halfWidthDp = (descriptor.params.bounds.width.value - childSize.value) / 2
                        val halfHeightDp = (descriptor.params.bounds.height.value - childSize.value) / 2
                        val radiusDp = min(halfWidthDp, halfHeightDp)  * 1.65f
                        val angleDegrees = transition.animateFloat(
                            transitionSpec = { spring() }, // TODO
                            targetValueByState = { when (it) {
                                is Active -> 0f
                                is InactiveAfter -> 0f
                                is InactiveBefore -> 0f
                                is Circular -> (it.offset * 1.0f / it.max) * 360
                            } }
                        )
                        val arcOffsetDp = derivedStateOf {
                            val angleRadians = Math.toRadians(angleDegrees.value.toDouble() - 90)
                            val x = radiusDp * cos(angleRadians)
                            val y = radiusDp * sin(angleRadians)
                            Offset(x.toFloat(), y.toFloat())
                        }
                        arcOffsetDp.value
                    }
                }
                offset
            },
        )

        val scale = transition.animateFloat(
            transitionSpec = { spring() } , // TODO
            targetValueByState = { when (it) {
                is Active -> 1f
                is InactiveAfter -> 1f
                is InactiveBefore -> 1f
                is Circular -> if (it.offset == 0) 0.25f else 0.125f
            } }
        )

        val alpha = transition.animateFloat(
            transitionSpec = { spring() } , // TODO
            targetValueByState = { when (it) {
                is Active -> 1f
                is InactiveAfter -> 1f
                is InactiveBefore -> 1f
                is Circular -> if (it.offset == 0) 1f else 0.5f
            } }
        )

        offset { IntOffset(x = offset.value.x.toInt(), y = offset.value.y.toInt()) }
            .scale(scale.value)
            .alpha(alpha.value)
    }

    private fun toOutsideRight(width: Float) = Offset(1.0f * width, 0f)

    private fun toOutsideLeft(width: Float) = Offset(-1.0f * width, 0f)

    private fun toCenter() = Offset(0f, 0f)
}

@Composable
fun <T> rememberSpotlightAdvancedSlider(
    childSize: Dp = 130.dp,
    transitionSpec: TransitionSpec<TransitionState, Offset> = { spring(stiffness = Spring.StiffnessVeryLow) },
    clipToBounds: Boolean = false,
): ModifierTransitionHandler<T, TransitionState> = remember {
    SpotlightAdvancedSlider(
        childSize = childSize,
        transitionSpec = transitionSpec,
        clipToBounds = clipToBounds
    )
}
