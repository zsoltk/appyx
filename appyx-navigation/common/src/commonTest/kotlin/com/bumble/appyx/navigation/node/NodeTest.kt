package com.bumble.appyx.navigation.node

import com.bumble.appyx.helpers.RootNode
import com.bumble.appyx.navigation.modality.NodeContext
import com.bumble.appyx.utils.multiplatform.SavedStateMap
import kotlin.test.Test
import kotlin.test.assertNotEquals

class NodeTest {
    @Test
    fun WHEN_node_is_create_THEN_plugins_are_setup_as_expected() {
        val savedStateMap: SavedStateMap = HashMap()
        val node = RootNode(NodeContext.root(savedStateMap))

        assertNotEquals(0, node.plugins.size, "Node should have some predefined plugins")
    }
}
