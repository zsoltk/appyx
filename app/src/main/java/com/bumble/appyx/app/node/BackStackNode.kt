package com.bumble.appyx.app.node

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumble.appyx.app.node.BackStackNode.Routing
import com.bumble.appyx.app.node.child.GenericChildNode
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.routingsource.backstack.BackStack
import com.bumble.appyx.routingsource.backstack.operation.pop
import com.bumble.appyx.routingsource.backstack.operation.push
import kotlin.random.Random
import kotlinx.android.parcel.Parcelize

class BackStackNode(
    buildContext: BuildContext,
    private val backStack: BackStack<Routing> = BackStack(
        initialElement = Routing.Child(),
        savedStateMap = buildContext.savedStateMap,
    ),
) : ParentNode<Routing>(
    routingSource = backStack,
    buildContext = buildContext
) {

    override fun resolve(routing: Routing, buildContext: BuildContext): Node {
        return when (routing) {
            is Routing.Child -> GenericChildNode(buildContext, Random.nextInt(0, 100))
        }
    }

    sealed class Routing : Parcelable {

        @Parcelize
        data class Child(val value: Int = Random.nextInt(0, 100)) : Routing()
    }

    @Composable
    override fun View(modifier: Modifier) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            Children(
                modifier = Modifier
                    .fillMaxSize(),
                routingSource = backStack,
                transitionHandler = rememberBackstackSlider()
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Button(onClick = { backStack.push(Routing.Child()) }) {
                    Text(text = "Push", color = Color.Black)
                }
                Spacer(modifier = Modifier.requiredWidth(16.dp))
                Button(onClick = { backStack.pop() }) {
                    Text(text = "Pop", color = Color.Black)
                }
            }
        }
    }


}