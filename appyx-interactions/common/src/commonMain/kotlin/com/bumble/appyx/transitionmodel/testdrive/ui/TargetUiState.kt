package com.bumble.appyx.transitionmodel.testdrive.ui

import com.bumble.appyx.interactions.core.Element
import com.bumble.appyx.interactions.core.model.progress.Draggable
import com.bumble.appyx.interactions.core.ui.context.UiContext
import com.bumble.appyx.interactions.core.ui.property.impl.BackgroundColor
import com.bumble.appyx.interactions.core.ui.property.impl.Position


class TargetUiState(
    val position: Position.Target,
    val backgroundColor: BackgroundColor.Target,
) {

    fun toMutableState(
        uiContext: UiContext,
        element: Element<*>,
        draggable: Draggable
    ): MutableUiState =
        MutableUiState(
            uiContext = uiContext,
            element = element,
            draggable = draggable,
            position = Position(uiContext, position),
            backgroundColor = BackgroundColor(uiContext, backgroundColor)
        )
}
