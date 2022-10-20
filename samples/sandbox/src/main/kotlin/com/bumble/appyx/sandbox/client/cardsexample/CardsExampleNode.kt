package com.bumble.appyx.sandbox.client.cardsexample

import android.os.Parcelable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.coroutineScope
import com.bumble.appyx.sandbox.client.cardsexample.CardsExampleNode.NavTarget
import com.bumble.appyx.samples.common.profile.Profile
import com.bumble.appyx.samples.common.profile.ProfileCardNode
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.cards.Cards
import com.bumble.appyx.navmodel.cards.operation.indicateLike
import com.bumble.appyx.navmodel.cards.operation.indicatePass
import com.bumble.appyx.navmodel.cards.operation.voteLike
import com.bumble.appyx.navmodel.cards.operation.votePass
import com.bumble.appyx.navmodel.cards.transitionhandler.rememberCardsTransitionHandler
import kotlinx.coroutines.delay
import kotlinx.parcelize.Parcelize

class CardsExampleNode(
    buildContext: BuildContext,
    private val cards: Cards<NavTarget> = Cards(
        initialItems = listOf(
            Profile.victoria,
            Profile.matt,
            Profile.zoe,
            Profile.sophia,

            Profile.imogen,

            Profile.brittany,
            Profile.ryan,
//            Profile.profile3002,

//            Profile.profile1003,
            Profile.chris,
//            Profile.profile3003,

//            Profile.profile001,
            Profile.daniel,


        ).map {
            NavTarget.ProfileCard(it)
        }
    ),
) : ParentNode<NavTarget>(
    buildContext = buildContext,
    navModel = cards
) {

    init {
        lifecycle.coroutineScope.launchWhenStarted {
            delay(2000)
            repeat(cards.elements.value.size / 4 + 1) {
                delay(1500)
                cards.indicateLike()
                delay(1000)
                cards.indicatePass()
                delay(1000)
                cards.votePass()
                delay(1000)
                cards.voteLike()
                delay(500)
                cards.voteLike()
                delay(500)
                cards.voteLike()
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
        val padding = remember { 20.dp }
        Children(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            navModel = cards,
            transitionHandler = rememberCardsTransitionHandler()
        ) {
            children<NavTarget> { child ->
                child(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

