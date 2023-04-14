package com.bumble.appyx.interactions.core.ui.gesture

import com.bumble.appyx.interactions.core.Element
import com.bumble.appyx.interactions.core.model.progress.Draggable

data class DraggableMapping<InteractionTarget>(
    val element: Element<InteractionTarget>,
    val draggable: Draggable
)
