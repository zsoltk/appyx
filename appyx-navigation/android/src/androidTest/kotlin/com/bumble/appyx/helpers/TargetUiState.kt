package com.bumble.appyx.helpers

import androidx.compose.animation.core.SpringSpec
import androidx.compose.ui.Modifier
import com.bumble.appyx.interactions.ui.context.UiContext
import com.bumble.appyx.interactions.ui.state.BaseMutableUiState
import kotlinx.coroutines.CoroutineScope

class TargetUiState(
    val id: Int,
)

class MutableUiState(
    uiContext: UiContext,
    val id: Int,
) : BaseMutableUiState<TargetUiState>(
    uiContext, emptyList()
) {
    override val combinedMotionPropertyModifier: Modifier = Modifier

    override suspend fun snapTo(target: TargetUiState) = Unit

    override fun lerpTo(
        scope: CoroutineScope,
        start: TargetUiState,
        end: TargetUiState,
        fraction: Float
    ) = Unit

    override suspend fun animateTo(
        scope: CoroutineScope,
        target: TargetUiState,
        springSpec: SpringSpec<Float>
    ) = Unit
}

fun TargetUiState.toMutableUiState(uiContext: UiContext): MutableUiState =
    MutableUiState(
        uiContext = uiContext,
        id = id,
    )
