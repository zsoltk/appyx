package com.bumble.appyx.v2.sandbox.client.mvicoreexample.feature

import com.bumble.appyx.v2.sandbox.client.mvicoreexample.MviCoreExampleView.Event
import com.bumble.appyx.v2.sandbox.client.mvicoreexample.feature.MviCoreExampleFeature.Wish

internal object EventsToWish : (Event) -> Wish? {
    override fun invoke(event: Event): Wish? =
        when (event) {
            is Event.LoadDataClicked -> Wish.LoadData
            else -> null
        }
}
