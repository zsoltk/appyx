package com.bumble.appyx.interactions.core.ui.state

import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.bumble.appyx.combineState
import com.bumble.appyx.interactions.Logger
import com.bumble.appyx.interactions.core.Element
import com.bumble.appyx.interactions.core.model.progress.Draggable
import com.bumble.appyx.interactions.core.ui.context.UiContext
import com.bumble.appyx.interactions.core.ui.gesture.DraggableMapping
import com.bumble.appyx.interactions.core.ui.property.MotionProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

abstract class BaseMutableUiState<MutableUiState, TargetUiState>(
    val uiContext: UiContext,
    val motionProperties: List<MotionProperty<*, *>>
) {
    abstract val modifier: Modifier

    protected var draggableMapping: DraggableMapping<*>? = null

    val isAnimating: Flow<Boolean>
        get() = combine(motionProperties.map { it.isAnimating }) { booleanArray ->
            booleanArray.any { it }
        }

    val isVisible: StateFlow<Boolean>
        get() = combineState(
            motionProperties.map { it.isVisibleFlow },
            uiContext.coroutineScope
        ) { booleanArray ->
            booleanArray.all { it }
        }

    abstract suspend fun snapTo(
        scope: CoroutineScope,
        target: TargetUiState
    )

    abstract fun lerpTo(
        scope: CoroutineScope,
        start: TargetUiState,
        end: TargetUiState,
        fraction: Float
    )

    abstract suspend fun animateTo(
        scope: CoroutineScope,
        target: TargetUiState,
        springSpec: SpringSpec<Float>,
    )

    fun makeDraggable(element: Element<*>, draggable: Draggable) {
        this.draggableMapping = DraggableMapping(element, draggable)
    }

    protected fun Modifier.dragGestures(): Modifier =
        apply {
            draggableMapping?.let {
                pointerInput(it.element.id) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            it.draggable.onDrag(dragAmount, this)
                        },
                        onDragEnd = {
                            Logger.log("drag", "end")
                            it.draggable.onDragEnd()
                        }
                    )
                }
            }
        }
}
