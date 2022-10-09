package com.bumble.appyx.sandbox2.navmodel2

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import com.bumble.appyx.sandbox2.navmodel2.NavTarget.*
import com.bumble.appyx.core.navigation2.inputsource.ManualProgressInputSource
import com.bumble.appyx.navmodel2.spotlight.Spotlight
import com.bumble.appyx.navmodel2.spotlight.operation.Next
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import com.bumble.appyx.navmodel2.spotlight.transitionhandler.SpotlightSlider
import com.bumble.appyx.sandbox2.ui.theme.appyx_dark


private val spotlight = Spotlight(
    items = listOf(Child1, Child2, Child3, Child4, Child5, Child6, Child7),
)


@ExperimentalMaterialApi
@Composable
fun SpotlightExperimentDebug() {
    val coroutineScope = rememberCoroutineScope()
    val inputSource = remember { ManualProgressInputSource(spotlight, coroutineScope) }

    LaunchedEffect(Unit) {
        delay(1500)
//        val stiffness = Spring.StiffnessVeryLow / 18
        val stiffness = Spring.StiffnessVeryLow
        inputSource.operation(Next(), spring(stiffness = stiffness))
        inputSource.operation(Next(), spring(stiffness = stiffness))
        inputSource.operation(Next(), spring(stiffness = stiffness))
        inputSource.operation(Next(), spring(stiffness = stiffness))
        inputSource.operation(Next(), spring(stiffness = stiffness))
        inputSource.operation(Next(), spring(stiffness = stiffness))
    }

    var elementSize by remember { mutableStateOf(IntSize(0, 0)) }
    val transitionParams by createTransitionParams(elementSize)
    val uiProps = remember(transitionParams) { SpotlightSlider<NavTarget>(transitionParams) }
    val render = remember(uiProps) { spotlight.elements.map { uiProps.map(it) } }

    Column(
        Modifier
            .fillMaxWidth()
            .background(appyx_dark)
    ) {
        KnobControl(onValueChange = {
            inputSource.setProgress(it)
        })

        Children(
            render = render.collectAsState(listOf()),
            onElementSizeChanged = { elementSize = it }
        )
    }
}