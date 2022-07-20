package com.bumble.appyx.routingsource.spotlightadvanced.transitionhandler

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import com.bumble.appyx.core.routing.transition.ModifierTransitionHandler
import com.bumble.appyx.core.routing.transition.TransitionSpec
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced

@Composable
fun <T> rememberSpotlightAdvancedSlider(
    transitionSpec: TransitionSpec<SpotlightAdvanced.TransitionState, Offset> = { spring(stiffness = Spring.StiffnessVeryLow) },
    clipToBounds: Boolean = false
): ModifierTransitionHandler<T, SpotlightAdvanced.TransitionState> = remember {
    SpotlightAdvancedSlider(transitionSpec = transitionSpec, clipToBounds = clipToBounds)
}
