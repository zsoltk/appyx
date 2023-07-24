package com.bumble.appyx.components.spotlight.ui.stack3d

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.bumble.appyx.interactions.core.ui.context.UiContext
import com.bumble.appyx.interactions.core.ui.math.clamp
import com.bumble.appyx.interactions.core.ui.math.smoothstep
import com.bumble.appyx.interactions.core.ui.property.impl.Alpha
import com.bumble.appyx.interactions.core.ui.property.impl.Position
import com.bumble.appyx.interactions.core.ui.property.impl.Scale
import com.bumble.appyx.interactions.core.ui.property.impl.ZIndex
import com.bumble.appyx.interactions.core.ui.state.MutableUiStateSpecs
import com.bumble.appyx.mapState
import kotlinx.coroutines.flow.StateFlow

@MutableUiStateSpecs
class TargetUiState(
    val positionInList: Int = 0,
    val position: Position.Target,
    val scale: Scale.Target,
    val alpha: Alpha.Target,
    val zIndex: ZIndex.Target,
) {
    constructor(
        base: TargetUiState,
        positionInList: Int,
    ) : this(
        positionInList = positionInList,
        position = base.position,
        scale = base.scale,
        alpha = base.alpha,
        zIndex = base.zIndex,
    )

    fun toMutableState(
        uiContext: UiContext,
        scrollX: StateFlow<Float>,
        itemHeight: Dp,
        itemsInStack: Int = 3,
    ): MutableUiState {
        return MutableUiState(
            uiContext = uiContext,
            position = Position(
                uiContext = uiContext,
                target = position,
                displacement = scrollX.mapState(uiContext.coroutineScope) {
                    val factor = 0.075f + smoothstep(0f, 1f, it)
                    Position.Value(
                        offset = DpOffset(
                            x = 0.dp,
                            y = (-factor * it * itemHeight.value / (1f - 0.1f * it)).dp,
                        )
                    )
                }
            ),
            scale = Scale(
                uiContext = uiContext,
                target = scale,
                displacement = scrollX.mapState(uiContext.coroutineScope) { -0.1f * it },
            ),
            alpha = Alpha(
                uiContext = uiContext,
                target = alpha,
                displacement = scrollX.mapState(uiContext.coroutineScope) {
                    clamp(it, 0f, 1f) + clamp(-it - itemsInStack, 0f, 1f)
                },
            ),
            zIndex = ZIndex(
                uiContext = uiContext,
                target = zIndex,
                displacement = scrollX.mapState(uiContext.coroutineScope) { -it }
            ),
        )
    }
}
