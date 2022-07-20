package com.bumble.appyx.routingsource.spotlightadvanced.transitionhandler

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.bumble.appyx.core.routing.transition.ModifierTransitionHandler
import com.bumble.appyx.core.routing.transition.TransitionDescriptor
import com.bumble.appyx.core.routing.transition.TransitionSpec
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced

@Suppress("TransitionPropertiesLabel")
class SpotlightAdvancedSlider<T>(
    private val transitionSpec: TransitionSpec<SpotlightAdvanced.TransitionState, Offset> = {
        spring(stiffness = Spring.StiffnessVeryLow)
    },
    override val clipToBounds: Boolean = false
) : ModifierTransitionHandler<T, SpotlightAdvanced.TransitionState>() {

    override fun createModifier(
        modifier: Modifier,
        transition: Transition<SpotlightAdvanced.TransitionState>,
        descriptor: TransitionDescriptor<T, SpotlightAdvanced.TransitionState>
    ): Modifier = modifier.composed {
        val offset = transition.animateOffset(
            transitionSpec = transitionSpec,
            targetValueByState = {
                val width = descriptor.params.bounds.width.value
                when (it) {
                    SpotlightAdvanced.TransitionState.INACTIVE_BEFORE -> toOutsideLeft(width)
                    SpotlightAdvanced.TransitionState.ACTIVE -> toCenter()
                    SpotlightAdvanced.TransitionState.INACTIVE_AFTER -> toOutsideRight(width)
                }
            },
        )

        offset(Dp(offset.value.x), Dp(offset.value.y))
    }

    private fun toOutsideRight(width: Float) = Offset(1.0f * width, 0f)

    private fun toOutsideLeft(width: Float) = Offset(-1.0f * width, 0f)

    private fun toCenter() = Offset(0f, 0f)
}
