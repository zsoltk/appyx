package com.bumble.appyx.routingsource.spotlightadvanced.transitionhandler

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import com.bumble.appyx.core.routing.transition.ModifierTransitionHandler
import com.bumble.appyx.core.routing.transition.TransitionDescriptor
import com.bumble.appyx.core.routing.transition.TransitionSpec
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced

@Suppress("TransitionPropertiesLabel")

class SpotlightAdvancedFader<T>(
    private val transitionSpec: TransitionSpec<SpotlightAdvanced.TransitionState, Float> = { spring() }
) : ModifierTransitionHandler<T, SpotlightAdvanced.TransitionState>() {

    override fun createModifier(
        modifier: Modifier,
        transition: Transition<SpotlightAdvanced.TransitionState>,
        descriptor: TransitionDescriptor<T, SpotlightAdvanced.TransitionState>
    ): Modifier = modifier.composed {
        val alpha = transition.animateFloat(
            transitionSpec = transitionSpec,
            targetValueByState = {
                when (it) {
                    SpotlightAdvanced.TransitionState.ACTIVE -> 1f
                    else -> 0f
                }
            })

        alpha(alpha.value)
    }
}

@Composable
fun <T> rememberSpotlightAdvancedFader(
    transitionSpec: TransitionSpec<SpotlightAdvanced.TransitionState, Float> = { spring() }
): ModifierTransitionHandler<T, SpotlightAdvanced.TransitionState> = remember {
    SpotlightAdvancedFader(transitionSpec = transitionSpec)
}
