package com.bumble.appyx.navmodel.maps

import com.bumble.appyx.navmodel.maps.Maps.TransitionState
import com.bumble.appyx.core.navigation.RoutingElement
import com.bumble.appyx.core.navigation.RoutingElements

typealias MapsElement<T> = RoutingElement<T, TransitionState>

typealias MapsElements<T> = RoutingElements<T, TransitionState>

