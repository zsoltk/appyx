package com.bumble.appyx.helpers

import androidx.compose.material.Text
import com.bumble.appyx.helpers.RootNode.NavTarget
import com.bumble.appyx.navigation.modality.NodeContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.navigation.node.node

class RootNode(
    nodeContext: NodeContext,
    dummyComponent: DummyComponent<NavTarget> = DummyComponent(
        model = DummyComponentModel(
            initialTarget = NavTarget.Child1,
            savedStateMap = nodeContext.savedStateMap
        ),
        visualisation = { DummyVisualisation(it) }
    )
) : Node<NavTarget>(
    nodeContext = nodeContext,
    appyxComponent = dummyComponent,
) {
    sealed interface NavTarget {
        data object Child1 : NavTarget
        data object Child2 : NavTarget
    }

    override fun buildChildNode(navTarget: NavTarget, nodeContext: NodeContext): Node<*> =
        when (navTarget) {
            is NavTarget.Child1 -> node(nodeContext) { Text("Child 1") }
            is NavTarget.Child2 -> node(nodeContext) { Text("Child 2") }
        }

}
