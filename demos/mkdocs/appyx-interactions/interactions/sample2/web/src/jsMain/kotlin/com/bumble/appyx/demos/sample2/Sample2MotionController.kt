package com.bumble.appyx.demos.sample2

import DefaultAnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.bumble.appyx.components.internal.testdrive.TestDriveModel
import com.bumble.appyx.components.internal.testdrive.TestDriveModel.State.ElementState.A
import com.bumble.appyx.components.internal.testdrive.TestDriveModel.State.ElementState.B
import com.bumble.appyx.components.internal.testdrive.TestDriveModel.State.ElementState.C
import com.bumble.appyx.components.internal.testdrive.TestDriveModel.State.ElementState.D
import com.bumble.appyx.components.internal.testdrive.operation.MoveTo
import com.bumble.appyx.interactions.AppyxLogger
import com.bumble.appyx.interactions.core.ui.context.TransitionBounds
import com.bumble.appyx.interactions.core.ui.context.UiContext
import com.bumble.appyx.interactions.core.ui.gesture.Gesture
import com.bumble.appyx.interactions.core.ui.gesture.GestureFactory
import com.bumble.appyx.interactions.core.ui.property.impl.BackgroundColor
import com.bumble.appyx.interactions.core.ui.property.impl.Position
import com.bumble.appyx.interactions.core.ui.property.impl.RotationZ
import com.bumble.appyx.interactions.core.ui.property.impl.RoundedCorners
import com.bumble.appyx.interactions.core.ui.property.impl.Scale
import com.bumble.appyx.interactions.core.ui.state.MatchedTargetUiState
import com.bumble.appyx.transitionmodel.BaseMotionController
import kotlin.math.abs

class Sample2MotionController<InteractionTarget : Any>(
    uiContext: UiContext,
    uiAnimationSpec: SpringSpec<Float> = DefaultAnimationSpec
) : BaseMotionController<InteractionTarget, TestDriveModel.State<InteractionTarget>, MutableUiState, TargetUiState>(
    uiContext = uiContext,
    defaultAnimationSpec = uiAnimationSpec,
) {
    override fun TestDriveModel.State<InteractionTarget>.toUiTargets(): List<MatchedTargetUiState<InteractionTarget, TargetUiState>> =
        listOf(
            MatchedTargetUiState(element, elementState.toTargetUiState()).also {
                AppyxLogger.d("TestDrive", "Matched $elementState -> UiState: ${it.targetUiState}")
            }
        )

    companion object {
        val offsetA = DpOffset(0.dp, 0.dp)
        val offsetB = DpOffset(324.dp, 0.dp)
        val offsetC = DpOffset(324.dp, 180.dp)
        val offsetD = DpOffset(0.dp, 180.dp)

        fun TestDriveModel.State.ElementState.toTargetUiState(): TargetUiState =
            when (this) {
                A -> uiStateA
                B -> uiStateB
                C -> uiStateC
                D -> uiStateD
            }

        // Top-left corner, A
        private val uiStateA = TargetUiState(
            position = Position.Target(offsetA),
            rotationZ = RotationZ.Target(0f),
            backgroundColor = BackgroundColor.Target(color_primary)
        )

        // Top-right corner, B
        private val uiStateB = TargetUiState(
            position = Position.Target(offsetB),
            rotationZ = RotationZ.Target(180f),
            backgroundColor = BackgroundColor.Target(color_dark)
        )

        // Bottom-right corner, C
        private val uiStateC = TargetUiState(
            position = Position.Target(offsetC),
            rotationZ = RotationZ.Target(270f),
            backgroundColor = BackgroundColor.Target(color_secondary)
        )

        // Bottom-left corner, D
        private val uiStateD = TargetUiState(
            position = Position.Target(offsetD),
            rotationZ = RotationZ.Target(540f),
            backgroundColor = BackgroundColor.Target(color_tertiary)
        )
    }

    override fun mutableUiStateFor(uiContext: UiContext, targetUiState: TargetUiState): MutableUiState =
        targetUiState.toMutableState(uiContext)

    class Gestures<InteractionTarget>(
        transitionBounds: TransitionBounds,
    ) : GestureFactory<InteractionTarget, TestDriveModel.State<InteractionTarget>> {
        private val width = offsetB.x - offsetA.x
        private val height = offsetD.y - offsetA.y

        override fun createGesture(
            delta: androidx.compose.ui.geometry.Offset,
            density: Density
        ): Gesture<InteractionTarget, TestDriveModel.State<InteractionTarget>> {
            val width = with(density) { width.toPx() }
            val height = with(density) { height.toPx() }

            return if (abs(delta.x) > abs(delta.y)) {
                if (delta.x < 0) {
                    Gesture(
                        operation = MoveTo(D),
                        dragToProgress = { offset -> (offset.x / width) * -1 },
                        partial = { offset, progress -> offset.copy(x = progress * width * -1) }
                    )
                } else {
                    Gesture(
                        operation = MoveTo(B),
                        dragToProgress = { offset -> (offset.x / width) },
                        partial = { offset, partial -> offset.copy(x = partial * width) }
                    )
                }
            } else {
                if (delta.y < 0) {
                    Gesture(
                        operation = MoveTo(A),
                        dragToProgress = { offset -> (offset.y / height) * -1 },
                        partial = { offset, partial -> offset.copy(y = partial * height * -1) }
                    )
                } else {
                    Gesture(
                        operation = MoveTo(C),
                        dragToProgress = { offset -> (offset.y / height) },
                        partial = { offset, partial -> offset.copy(y = partial * height) }
                    )
                }
            }
        }
    }
}

