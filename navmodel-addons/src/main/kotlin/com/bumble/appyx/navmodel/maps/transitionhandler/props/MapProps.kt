package com.bumble.appyx.navmodel.maps.transitionhandler.props

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import com.bumble.appyx.core.navigation.transition.TransitionDescriptor
import com.bumble.appyx.core.navigation.transition.TransitionSpec
import com.bumble.appyx.navmodel.maps.Maps

@Suppress("TransitionPropertiesLabel")
internal fun Modifier.mapModifier(
    transitionSpec: TransitionSpec<Maps.State, Float> = { tween(500) },
    transition: Transition<Maps.State>,
    descriptor: TransitionDescriptor<Maps.Target, Maps.State>
): Modifier = composed {
    data class Props(
        val scale: Float = 1f,
    )

    val default = Props(scale = 4f)
    val zoomedOut = Props(scale = 1f)
    fun stateToProps(state: Maps.State): Props =
        when (state) {
            Maps.State.CREATED -> default
            Maps.State.DEFAULT -> default
            Maps.State.VENUES_LIST -> zoomedOut
            Maps.State.VENUES_PAGER -> zoomedOut
            Maps.State.VENUE_SHOW -> default
            Maps.State.DESTROYED -> default
        }

    val scale = transition.animateFloat(
        transitionSpec = transitionSpec,
        targetValueByState = {
            stateToProps(it).scale
        })

    scale(scale.value)
}