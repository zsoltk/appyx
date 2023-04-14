package com.bumble.appyx.transitionmodel.testdrive.ui

import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.bumble.appyx.interactions.Logger
import com.bumble.appyx.interactions.core.Element
import com.bumble.appyx.interactions.core.model.progress.Draggable
import com.bumble.appyx.interactions.core.ui.context.UiContext
import com.bumble.appyx.interactions.core.ui.property.impl.BackgroundColor
import com.bumble.appyx.interactions.core.ui.property.impl.Position
import com.bumble.appyx.interactions.core.ui.state.BaseMutableUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class MutableUiState(
    uiContext: UiContext,
    private val draggable: Draggable,
    private val element: Element<*>,
    private val position: Position,
    private val backgroundColor: BackgroundColor,
) : BaseMutableUiState<MutableUiState, TargetUiState>(
    uiContext = uiContext,
    motionProperties = listOf(position, backgroundColor)
) {

    override val modifier: Modifier
        get() = Modifier.then(position.modifier)
            .then(backgroundColor.modifier)
            .pointerInput(element.id) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        draggable.onDrag(dragAmount, this)
                    },
                    onDragEnd = {
                        Logger.log("drag", "end")
                        draggable.onDragEnd()
                    }
                )
            }

    override suspend fun snapTo(scope: CoroutineScope, target: TargetUiState) {
        scope.launch {
            position.snapTo(target.position.value)
            backgroundColor.snapTo(target.backgroundColor.value)
        }
    }

    override suspend fun animateTo(
        scope: CoroutineScope,
        target: TargetUiState,
        springSpec: SpringSpec<Float>,
    ) {
        listOf(
            scope.async {
                position.animateTo(
                    target.position.value,
                    spring(springSpec.dampingRatio, springSpec.stiffness)
                )
            },
            scope.async {
                backgroundColor.animateTo(
                    target.backgroundColor.value,
                    spring(springSpec.dampingRatio, springSpec.stiffness)
                )
            }
        ).awaitAll()
    }
    override fun lerpTo(scope: CoroutineScope, start: TargetUiState, end: TargetUiState, fraction: Float) {
        scope.launch {
            position.lerpTo(start.position, end.position, fraction)
            backgroundColor.lerpTo(start.backgroundColor, end.backgroundColor, fraction)
        }
    }
}
