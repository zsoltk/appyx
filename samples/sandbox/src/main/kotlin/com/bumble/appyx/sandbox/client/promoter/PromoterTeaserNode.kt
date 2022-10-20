package com.bumble.appyx.sandbox.client.promoter

import android.os.Parcelable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.lifecycle.coroutineScope
import com.bumble.appyx.navmodel.promoter.navmodel.Promoter
import com.bumble.appyx.navmodel.promoter.navmodel.operation.addFirst
import com.bumble.appyx.navmodel.promoter.navmodel.operation.promoteAll
import com.bumble.appyx.navmodel.promoter.transitionhandler.rememberPromoterTransitionHandler
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.sandbox.client.promoter.PromoterTeaserNode.NavTarget
import com.bumble.appyx.samples.common.profile.Profile
import com.bumble.appyx.samples.common.profile.ProfileCardNode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@ExperimentalUnitApi
class PromoterTeaserNode(
    buildContext: BuildContext,
    private val promoter: Promoter<NavTarget> = Promoter(),
) : ParentNode<NavTarget>(
    buildContext = buildContext,
    navModel = promoter
) {

    val profiles = listOf(
        Profile.victoria,
        Profile.matt,
        Profile.zoe,
        Profile.sophia,

        Profile.imogen,

        Profile.brittany,
        Profile.ryan,
        Profile.chris,
        Profile.daniel
    )

    init {
        var i = 0
        lifecycle.coroutineScope.launch {
            repeat(4) {
                promoter.addFirst(NavTarget.ProfileCard(profiles.get(i++ % profiles.size)))
                promoter.promoteAll()
            }
            delay(3500)
            repeat(6) {
                delay(1500)
                promoter.addFirst(NavTarget.ProfileCard(profiles.get(i++ % profiles.size)))
                promoter.promoteAll()
            }
        }
    }

    sealed class NavTarget : Parcelable {
        @Parcelize
        class ProfileCard(val profile: Profile) : NavTarget()
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node =
        when (navTarget) {
            is NavTarget.ProfileCard -> ProfileCardNode(buildContext, navTarget.profile)
        }

    @Composable
    override fun View(modifier: Modifier) {
        val childSize = remember { 100.dp }
        Children(
            modifier = Modifier.fillMaxSize(),
            navModel = promoter,
            transitionHandler = rememberPromoterTransitionHandler(childSize) {
                spring(stiffness = Spring.StiffnessVeryLow / 4)
            }
        ) {
            children<NavTarget> { child ->
                child(Modifier.size(childSize))
            }
        }
    }
}

