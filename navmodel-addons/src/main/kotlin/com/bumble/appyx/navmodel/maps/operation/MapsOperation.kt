package com.bumble.appyx.navmodel.maps.operation

import com.bumble.appyx.navmodel.maps.Maps.State
import com.bumble.appyx.core.navigation.Operation
import com.bumble.appyx.navmodel.maps.Maps

sealed interface MapsOperation : Operation<Maps.Target, State>
