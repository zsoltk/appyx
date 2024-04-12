package com.bumble.appyx.navigation.node

import com.bumble.appyx.helpers.DummyComponent
import com.bumble.appyx.helpers.DummyComponentModel
import com.bumble.appyx.helpers.DummyVisualisation
import com.bumble.appyx.helpers.RootNode
import com.bumble.appyx.navigation.AppyxTestScenario
import com.bumble.appyx.navigation.modality.NodeContext
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test

class NodeTest {
    private var appyxComponent: DummyComponent<RootNode.NavTarget>? = null

    private val nodeFactory: (nodeContext: NodeContext) -> Node<*> = { nodeContext ->
        appyxComponent = DummyComponent(
            model = DummyComponentModel(
                initialTarget = RootNode.NavTarget.Child1,
                savedStateMap = nodeContext.savedStateMap
            ),
            visualisation = { DummyVisualisation(it) }
        )
        RootNode(nodeContext = nodeContext, appyxComponent = appyxComponent!!)
    }

    @get:Rule
    val rule = AppyxTestScenario { nodeContext ->
        nodeFactory(nodeContext)
    }

    @Test
    fun WHEN_node_is_create_THEN_plugins_are_setup_as_expected() {
        rule.start()
        assertNotEquals("Node should have some predefined plugins", 0, rule.node.plugins.size)
    }

    @Test
    fun WHEN_node_is_create_THEN_appyx_component_state_is_saved_during_recreation() {
        rule.start()
        appyxComponent!!.resetSaveInstanceState()
        rule.activityScenario.recreate()
        assertNotEquals(
            "AppyxComponent state should be saved",
            0,
            appyxComponent!!.saveInstanceStateInvoked
        )
    }

}
