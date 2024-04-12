package com.bumble.appyx.navigation.node

import com.bumble.appyx.helpers.RootNode
import com.bumble.appyx.navigation.AppyxTestScenario
import com.bumble.appyx.navigation.modality.NodeContext
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test

class NodeTest {
    var nodeFactory: (nodeContext: NodeContext) -> Node<*> = {
        RootNode(nodeContext = it)
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
}
