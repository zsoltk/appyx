package com.bumble.appyx.routingsource.spotlightadvanced

import kotlinx.coroutines.flow.map

fun <T : Any> SpotlightAdvanced<T>.hasNext() =
    elements.map { value -> value.lastIndex != elements.value.currentIndex }

fun <T : Any> SpotlightAdvanced<T>.hasPrevious() =
    elements.map { value -> value.currentIndex != 0 }

fun <T : Any> SpotlightAdvanced<T>.activeIndex() =
    elements.map { value -> value.currentIndex }

fun <T : Any> SpotlightAdvanced<T>.elementsCount() =
    elements.value.size
