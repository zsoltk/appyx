package com.bumble.appyx.routingsource.spotlightadvanced

import com.bumble.appyx.core.routing.RoutingElement
import com.bumble.appyx.core.routing.RoutingElements
import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState

typealias SpotlightAdvancedElement<T> = RoutingElement<T, TransitionState>

typealias SpotlightAdvancedElements<T> = RoutingElements<T, TransitionState>
