package com.bumble.appyx.components.experimental.promoter.android

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.bumble.appyx.components.experimental.promoter.Promoter
import com.bumble.appyx.components.experimental.promoter.PromoterModel
import com.bumble.appyx.components.experimental.promoter.operation.addFirst
import com.bumble.appyx.components.experimental.promoter.ui.PromoterVisualisation
import com.bumble.appyx.interactions.composable.AppyxInteractionsContainer
import com.bumble.appyx.interactions.model.transition.Operation.Mode.IMMEDIATE
import com.bumble.appyx.interactions.model.transition.Operation.Mode.KEYFRAME
import com.bumble.appyx.interactions.ui.helper.AppyxComponentSetup
import com.bumble.appyx.interactions.utils.testing.TestTarget
import com.bumble.appyx.interactions.utils.testing.TestTarget.Child1
import com.bumble.appyx.interactions.utils.testing.TestTarget.Child2
import com.bumble.appyx.interactions.utils.testing.TestTarget.Child3
import com.bumble.appyx.interactions.utils.testing.TestTarget.Child4
import com.bumble.appyx.interactions.utils.ui.Element
import kotlin.math.roundToInt


@Suppress("MagicNumber", "LongMethod")
@ExperimentalMaterialApi
@Composable
fun PromoterExperiment(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()

    val promoter = remember {
        Promoter(
            scope = coroutineScope,
            model = PromoterModel<TestTarget>(savedStateMap = null),
            visualisation = {
                PromoterVisualisation(
                    uiContext = it
                )
            },
            animationSpec = spring(stiffness = Spring.StiffnessVeryLow / 20)
        )
    }

    AppyxComponentSetup(promoter)

    LaunchedEffect(Unit) {
        promoter.addFirst(Child1)
        promoter.addFirst(Child2)
        promoter.addFirst(Child3)
        promoter.addFirst(Child4)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        val density = LocalDensity.current
        val screenWidthPx =
            (LocalConfiguration.current.screenWidthDp * density.density).roundToInt()
        val screenHeightPx =
            (LocalConfiguration.current.screenHeightDp * density.density).roundToInt()

        AppyxInteractionsContainer(
            appyxComponent = promoter,
            modifier = Modifier
                .weight(0.9f)
                .padding(
                    horizontal = 64.dp,
                    vertical = 12.dp
                ),
            elementUi = {
                Element(
                    element = it,
                    modifier = Modifier.size(100.dp)
                )
            },
            screenWidthPx = screenWidthPx,
            screenHeightPx = screenHeightPx,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
                .padding(4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { promoter.addFirst(TestTarget.entries.random(), KEYFRAME) }
            ) {
                Text("KEYFRAME")
            }
            Spacer(Modifier.size(24.dp))
            Button(
                onClick = { promoter.addFirst(TestTarget.entries.random(), IMMEDIATE) }
            ) {
                Text("IMMEDIATE")
            }
        }
    }
}
