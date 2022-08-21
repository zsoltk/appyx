package com.bumble.appyx.navmodel.maps.transitionhandler.props

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.navigation.transition.TransitionDescriptor
import com.bumble.appyx.core.navigation.transition.TransitionSpec
import com.bumble.appyx.navmodel.maps.Maps

@Suppress("TransitionPropertiesLabel")
internal fun Modifier.searchModifier(
    transitionSpec: TransitionSpec<Maps.State, Float> = { spring(stiffness = Spring.StiffnessVeryLow) },
    transition: Transition<Maps.State>,
    descriptor: TransitionDescriptor<Maps.Target, Maps.State>
): Modifier = composed {

    data class Props(
        val alpha: Float = 1f,
        val offsetY: Dp
    )

    val shown = Props(alpha = 1f, offsetY = 0.dp)
    val hidden = Props(alpha = 0f, offsetY = -(80).dp)

    fun stateToProps(state: Maps.State): Props =
        when (state) {
            Maps.State.CREATED -> shown
            Maps.State.DEFAULT -> shown
            Maps.State.VENUES_LIST -> shown
            Maps.State.VENUES_PAGER -> shown
            Maps.State.VENUE_SHOW -> hidden
            Maps.State.DESTROYED -> hidden
        }

    val alpha = transition.animateFloat(
        transitionSpec = transitionSpec,
        targetValueByState = {
            stateToProps(it).alpha
        })

    val offsetY = transition.animateFloat(
        transitionSpec = transitionSpec,
        targetValueByState = {
            stateToProps(it).offsetY.value
        })


    alpha(alpha.value)
        .offset(x = 0.dp, y = Dp(offsetY.value))
}