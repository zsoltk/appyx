package com.bumble.appyx.navmodel.maps.transitionhandler.props

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.navigation.transition.TransitionDescriptor
import com.bumble.appyx.core.navigation.transition.TransitionSpec
import com.bumble.appyx.navmodel.maps.Maps

@Suppress("TransitionPropertiesLabel")
internal fun Modifier.venuePagerModifier(
    transitionSpec: TransitionSpec<Maps.State, Float> = { spring(stiffness = Spring.StiffnessVeryLow) },
    transition: Transition<Maps.State>,
    descriptor: TransitionDescriptor<Maps.Target, Maps.State>
): Modifier = composed {

    data class Props(
        val alpha: Float,
        val heightRatio: Float,
        val offsetY: Dp
    )

    val hidden = Props(alpha = 0f, heightRatio = 0.3f, offsetY = -(80).dp)
    val shown = Props(alpha = 1f, heightRatio = 0.3f, offsetY = 0.dp)
    val fullScreen = shown.copy(heightRatio = 1f)

    fun stateToProps(state: Maps.State): Props =
        when (state) {
            Maps.State.CREATED -> hidden
            Maps.State.DEFAULT -> hidden
            Maps.State.VENUES_LIST -> hidden
            Maps.State.VENUES_PAGER -> shown
            Maps.State.VENUE_SHOW -> fullScreen
            Maps.State.DESTROYED -> hidden
        }

    val screenHeight = LocalConfiguration.current.screenHeightDp

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

    val heightRatio = transition.animateFloat(
        transitionSpec = transitionSpec,
        targetValueByState = {
            stateToProps(it).heightRatio
        })


    offset(x = Dp(0f), y = Dp(screenHeight * (1 - heightRatio.value) + offsetY.value))
        .fillMaxWidth()
        .fillMaxHeight(heightRatio.value)
        .alpha(alpha.value)
}