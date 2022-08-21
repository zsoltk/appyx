package com.bumble.appyx.navmodel.maps.operation

import com.bumble.appyx.navmodel.maps.Maps.TransitionState
import com.bumble.appyx.core.navigation.Operation

sealed interface MapsOperation<T> : Operation<T, TransitionState>
