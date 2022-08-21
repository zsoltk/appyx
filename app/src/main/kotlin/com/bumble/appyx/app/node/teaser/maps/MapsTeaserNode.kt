package com.bumble.appyx.app.node.teaser.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.coroutineScope
import com.bumble.appyx.core.composable.Child
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.navigation.RoutingElement
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.node
import com.bumble.appyx.navmodel.maps.Maps
import com.bumble.appyx.navmodel.maps.operation.defaultMode
import com.bumble.appyx.navmodel.maps.operation.venueListMode
import com.bumble.appyx.navmodel.maps.operation.venuePagerMode
import com.bumble.appyx.navmodel.maps.operation.venueShowMode
import com.bumble.appyx.navmodel.maps.transitionhandler.rememberMapsTransitionHandler
import kotlinx.coroutines.delay

@ExperimentalUnitApi
class MapsTeaserNode(
    buildContext: BuildContext,
    private val maps: Maps = Maps(
        savedStateMap = buildContext.savedStateMap,
    ),
) : ParentNode<Maps.Target>(
    buildContext = buildContext,
    navModel = maps
) {

    init {
        lifecycle.coroutineScope.launchWhenResumed {
            repeat(40) {
                delay(2000)
                maps.venueListMode()
                delay(2000)
                maps.venuePagerMode()
                delay(2000)
                maps.venueShowMode()
                delay(2000)
                maps.defaultMode()
                delay(2000)
            }
        }
    }

    override fun resolve(routing: Maps.Target, buildContext: BuildContext): Node =
        when (routing) {
            Maps.Target.MAP -> node(buildContext) { modifier -> Map(modifier) }
            Maps.Target.SEARCH -> node(buildContext) { modifier -> Search(modifier) }
            Maps.Target.FILTERS -> node(buildContext) { modifier -> Filters(modifier) }
            Maps.Target.CATEGORIES -> node(buildContext) { modifier -> Categories(modifier) }
            Maps.Target.VENUE_LIST -> node(buildContext) { modifier -> VenueList(modifier) }
            Maps.Target.VENUE_PAGER -> node(buildContext) { modifier -> VenuePager(modifier) }
        }

    @Composable
    private fun Map(modifier: Modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .zIndex(-1f)
            ,
            contentAlignment = Alignment.Center
        ) {
            Text("Map")
        }
    }

    @Composable
    private fun Search(modifier: Modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(Color.LightGray, RoundedCornerShape(24.dp))
                .padding(12.dp)
                .zIndex(2f)
        ) {
            Text("Search input", color = Color.Black)
        }
    }

    @Composable
    private fun Filters(modifier: Modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(Color.White)
                .padding(12.dp)
                .zIndex(1f)
        ) {

            Text("Filters", color = Color.Black)
        }
    }

    @Composable
    private fun Categories(modifier: Modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(Color.Blue)
                .padding(12.dp)
                .zIndex(1f)
        ) {
            Text("Categories")
        }
    }

    @Composable
    private fun VenueList(modifier: Modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Red)
                .zIndex(2f)
        ) {
            Text("VenueList")
        }
    }

    @Composable
    private fun VenuePager(modifier: Modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Green)
                .zIndex(2f)
        ) {
            Text("VenuePager", color = Color.Black)
        }
    }


    @Composable
    override fun View(modifier: Modifier) {
        val elements = maps.elements.collectAsState()
        val map = elements.value.find { it.key.routing == Maps.Target.MAP }!!
        val search = elements.value.find { it.key.routing == Maps.Target.SEARCH }!!
        val filters = elements.value.find { it.key.routing == Maps.Target.FILTERS }!!
        val categories = elements.value.find { it.key.routing == Maps.Target.CATEGORIES }!!
        val venueList = elements.value.find { it.key.routing == Maps.Target.VENUE_LIST }!!
        val venuePager = elements.value.find { it.key.routing == Maps.Target.VENUE_PAGER }!!
        val transitionHandler = rememberMapsTransitionHandler()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Child(map, transitionHandler)

            Column {
                Child(search, transitionHandler)
                Box {
                    Child(filters, transitionHandler)
                    Child(categories, transitionHandler)
                }
                Box {
                    Child(venueList, transitionHandler)
                    Child(venuePager, transitionHandler)
                }
            }
        }

//        Children(
//            modifier = Modifier.fillMaxSize(),
//            navModel = maps,
//            transitionHandler = rememberMapsTransitionHandler()
//        ) {
//            children<Maps.Target> { child ->
//                child(Modifier.size(childSize))
//            }
//        }
    }
}

