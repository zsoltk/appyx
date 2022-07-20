package com.bumble.appyx.routingsource.spotlightadvanced

import com.bumble.appyx.routingsource.spotlightadvanced.SpotlightAdvanced.TransitionState.Active

val <T> SpotlightAdvancedElements<T>.current: SpotlightAdvancedElement<T>?
    get() = this.lastOrNull { it.targetState == Active }

val <T> SpotlightAdvancedElements<T>.currentIndex: Int
    get() = this.indexOfLast { it.targetState == Active }
