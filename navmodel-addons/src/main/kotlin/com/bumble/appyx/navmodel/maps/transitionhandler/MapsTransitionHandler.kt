package com.bumble.appyx.navmodel.maps.transitionhandler

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.bumble.appyx.navmodel.maps.Maps.State
import com.bumble.appyx.core.navigation.transition.ModifierTransitionHandler
import com.bumble.appyx.core.navigation.transition.TransitionDescriptor
import com.bumble.appyx.core.navigation.transition.TransitionSpec
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.transitionhandler.BackStackFader
import com.bumble.appyx.navmodel.maps.Maps
import com.bumble.appyx.navmodel.maps.Maps.Target.*
import com.bumble.appyx.navmodel.maps.transitionhandler.props.categoriesModifier
import com.bumble.appyx.navmodel.maps.transitionhandler.props.filtersModifier
import com.bumble.appyx.navmodel.maps.transitionhandler.props.mapModifier
import com.bumble.appyx.navmodel.maps.transitionhandler.props.searchModifier
import com.bumble.appyx.navmodel.maps.transitionhandler.props.venueListModifier
import com.bumble.appyx.navmodel.maps.transitionhandler.props.venuePagerModifier

@Suppress("TransitionPropertiesLabel")
class MapsTransitionHandler(
    private val transitionSpec: TransitionSpec<State, Float> = { spring(stiffness = Spring.StiffnessVeryLow) }
) : ModifierTransitionHandler<Maps.Target, State>() {

    @SuppressLint("ModifierFactoryExtensionFunction")
    override fun createModifier(
        modifier: Modifier,
        transition: Transition<State>,
        descriptor: TransitionDescriptor<Maps.Target, State>
    ): Modifier =
        when (descriptor.element) {
            MAP ->  modifier.mapModifier(transitionSpec, transition, descriptor)
            SEARCH -> modifier.searchModifier(transitionSpec, transition, descriptor)
            FILTERS -> modifier.filtersModifier(transitionSpec, transition, descriptor)
            CATEGORIES -> modifier.categoriesModifier(transitionSpec, transition, descriptor)
            VENUE_LIST -> modifier.venueListModifier(transitionSpec, transition, descriptor)
            VENUE_PAGER -> modifier.venuePagerModifier(transitionSpec, transition, descriptor)
    }
}

@Composable
fun rememberMapsTransitionHandler(
    transitionSpec: TransitionSpec<State, Float> = { spring(stiffness = Spring.StiffnessVeryLow) }
): MapsTransitionHandler = remember {
    MapsTransitionHandler(transitionSpec = transitionSpec)
}
