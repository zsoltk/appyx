package com.bumble.appyx.helpers

import com.bumble.appyx.helpers.DummyComponentModel.State
import com.bumble.appyx.interactions.model.Element
import com.bumble.appyx.interactions.model.asElement
import com.bumble.appyx.interactions.model.transition.BaseTransitionModel
import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize
import com.bumble.appyx.utils.multiplatform.SavedStateMap

class DummyComponentModel<NavTarget : Any>(
    initialTarget: NavTarget,
    savedStateMap: SavedStateMap?,
) : BaseTransitionModel<NavTarget, State<NavTarget>>(
    savedStateMap = savedStateMap,
) {
    @Parcelize
    data class State<NavTarget>(
        val target: Element<NavTarget>
    ) : Parcelable

    override val initialState: State<NavTarget> = State(initialTarget.asElement())

    override fun State<NavTarget>.availableElements(): Set<Element<NavTarget>> = setOf(target)

    override fun State<NavTarget>.removeDestroyedElement(element: Element<NavTarget>): State<NavTarget> =
        this

    override fun State<NavTarget>.removeDestroyedElements(): State<NavTarget> = this

    override fun State<NavTarget>.destroyedElements(): Set<Element<NavTarget>> = emptySet()
}
