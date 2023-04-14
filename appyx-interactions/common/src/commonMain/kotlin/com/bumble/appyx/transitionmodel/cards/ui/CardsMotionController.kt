package com.bumble.appyx.transitionmodel.cards.ui

import DefaultAnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.bumble.appyx.interactions.Logger
import com.bumble.appyx.interactions.core.model.transition.Operation
import com.bumble.appyx.interactions.core.ui.context.TransitionBounds
import com.bumble.appyx.interactions.core.ui.context.UiContext
import com.bumble.appyx.interactions.core.ui.gesture.Gesture
import com.bumble.appyx.interactions.core.ui.gesture.GestureFactory
import com.bumble.appyx.interactions.core.ui.property.impl.Position
import com.bumble.appyx.interactions.core.ui.property.impl.RotationZ
import com.bumble.appyx.interactions.core.ui.property.impl.Scale
import com.bumble.appyx.interactions.core.ui.property.impl.ZIndex
import com.bumble.appyx.interactions.core.ui.state.UiMapping
import com.bumble.appyx.transitionmodel.BaseMotionController
import com.bumble.appyx.transitionmodel.cards.CardsModel
import com.bumble.appyx.transitionmodel.cards.CardsModel.State.Card.InvisibleCard.VotedCard.VOTED_CARD_STATE.LIKED
import com.bumble.appyx.transitionmodel.cards.operation.VoteLike
import com.bumble.appyx.transitionmodel.cards.operation.VotePass

class CardsMotionController<InteractionTarget : Any>(
    uiContext: UiContext,
    defaultAnimationSpec: SpringSpec<Float> = DefaultAnimationSpec
) : BaseMotionController<InteractionTarget, CardsModel.State<InteractionTarget>, MutableUiState, TargetUiState>(
    uiContext = uiContext,
    defaultAnimationSpec = defaultAnimationSpec,
) {
    private val hidden = TargetUiState(
        scale = Scale.Target(0f)
    )

    private val bottom = TargetUiState(
        scale = Scale.Target(0.85f)
    )

    private val top = TargetUiState(
        scale = Scale.Target(1f),
        zIndex = ZIndex.Target(1f),
    )

    private val votePass = TargetUiState(
        position = Position.Target(
            DpOffset(
                x = (-voteCardPositionMultiplier * uiContext.transitionBounds.widthDp.value).dp,
                y = 0.dp
            )
        ),
        scale = Scale.Target(1f),
        zIndex = ZIndex.Target(2f),
        rotationZ = RotationZ.Target(-45f)
    )

    private val voteLike = TargetUiState(
        position = Position.Target(
            DpOffset(
                x = (voteCardPositionMultiplier * uiContext.transitionBounds.widthDp.value).dp,
                y = 0.dp
            )
        ),
        scale = Scale.Target(1f),
        zIndex = ZIndex.Target(2f),
        rotationZ = RotationZ.Target(45f)
    )

    override fun CardsModel.State<InteractionTarget>.toUiTargets(): List<UiMapping<InteractionTarget, TargetUiState>> {
        val result = mutableListOf<UiMapping<InteractionTarget, TargetUiState>>()
        (votedCards + visibleCards + queued).map {
            when (it) {
                is CardsModel.State.Card.InvisibleCard.VotedCard -> {
                    result.add(
                        if (it.votedCardState == LIKED) {
                            UiMapping(it.element, voteLike)
                        } else {
                            UiMapping(it.element, votePass)
                        }
                    )
                }

                is CardsModel.State.Card.VisibleCard.TopCard -> {
                    result.add(UiMapping(it.element, top))
                }

                is CardsModel.State.Card.VisibleCard.BottomCard -> {
                    result.add(UiMapping(it.element, bottom))
                }

                is CardsModel.State.Card.InvisibleCard.Queued -> {
                    result.add(UiMapping(it.element, hidden))
                }
            }
        }

        return result
    }

    override fun mutableUiStateFor(uiContext: UiContext, uiMapping: UiMapping<*, TargetUiState>): MutableUiState =
        uiMapping.targetUiState.toMutableState(uiContext)

    class Gestures<InteractionTarget>(
        transitionBounds: TransitionBounds
    ) : GestureFactory<InteractionTarget, CardsModel.State<InteractionTarget>> {

        private val width = transitionBounds.widthPx
        private val height = transitionBounds.widthPx

        private var touchPosition: Offset? = null

        override fun onStartDrag(position: Offset) {
            touchPosition = position
        }

        override fun createGesture(
            delta: Offset,
            density: Density
        ): Gesture<InteractionTarget, CardsModel.State<InteractionTarget>> {
            val verticalRatio = (touchPosition?.y ?: 0f) / height // 0..1
            // For a perfect solution we should keep track where the touch is currently at, at any
            // given moment; then do the calculation in reverse, how much of a horizontal gesture
            // at that vertical position should move the cards.
            // For a good enough solution, now we only care for the initial touch position and
            // a baked in factor to account for the top of the card moving with different speed than
            // the bottom:
            // e.g. 4 = at top of the card, 2 = at the bottom, when voteCardPositionMultiplier = 2
            val dragToProgressFactor = voteCardPositionMultiplier * (2 - verticalRatio)
            Logger.log("Cards", "delta ${delta.x}")

            return if (delta.x < 0) {
                Gesture(
                    operation = VotePass(Operation.Mode.KEYFRAME),
                    dragToProgress = { offset -> offset.x / (dragToProgressFactor * width) * -1 },
                    partial = { offset, progress -> offset.copy(x = progress * width * -1) }
                )
            } else {
                Gesture(
                    operation = VoteLike(Operation.Mode.KEYFRAME),
                    dragToProgress = { offset -> offset.x / (dragToProgressFactor * width) },
                    partial = { offset, progress -> offset.copy(x = progress * width) }
                )
            }
        }
    }

    private companion object {
        private const val voteCardPositionMultiplier = 2
    }
}
