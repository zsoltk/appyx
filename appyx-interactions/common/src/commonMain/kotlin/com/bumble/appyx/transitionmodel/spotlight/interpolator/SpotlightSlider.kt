package com.bumble.appyx.transitionmodel.spotlight.interpolator

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bumble.appyx.interactions.core.TransitionModel
import com.bumble.appyx.interactions.core.inputsource.Gesture
import com.bumble.appyx.interactions.core.ui.FrameModel
import com.bumble.appyx.interactions.core.ui.GestureFactory
import com.bumble.appyx.interactions.core.ui.Interpolator
import com.bumble.appyx.interactions.core.ui.Interpolator.Companion.lerpDpOffset
import com.bumble.appyx.interactions.core.ui.Interpolator.Companion.lerpFloat
import com.bumble.appyx.interactions.core.ui.MatchedProps
import com.bumble.appyx.interactions.core.ui.TransitionBounds
import com.bumble.appyx.interactions.core.ui.geometry.Geometry1D
import com.bumble.appyx.transitionmodel.spotlight.SpotlightModel
import com.bumble.appyx.transitionmodel.spotlight.SpotlightModel.State.ElementState.CREATED
import com.bumble.appyx.transitionmodel.spotlight.SpotlightModel.State.ElementState.DESTROYED
import com.bumble.appyx.transitionmodel.spotlight.SpotlightModel.State.ElementState.STANDARD
import com.bumble.appyx.transitionmodel.spotlight.operation.Next
import com.bumble.appyx.transitionmodel.spotlight.operation.Previous
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

class SpotlightSlider<NavTarget>(
    transitionBounds: TransitionBounds,
    private val scope: CoroutineScope,
    private val orientation: Orientation = Orientation.Horizontal, // TODO support RTL
) : Interpolator<NavTarget, SpotlightModel.State<NavTarget>> {
    private val width = transitionBounds.widthDp
    private val height = transitionBounds.heightDp

    private val scroll = Geometry1D<TransitionModel.Segment<SpotlightModel.State<NavTarget>>, List<FrameModel<NavTarget>>>(
        scope = scope,
        initialValue = 0f
    ) {
        mapFrame(it)
    }

    data class Props(
        val offset: DpOffset = DpOffset(0.dp, 0.dp),
        val scale: Float = 1f,
        val alpha: Float = 1f,
        val zIndex: Float = 1f,
        val aspectRatio: Float = 0.42f,
        val rotation: Float = 0f,
    )

    private val created = Props(
        offset = DpOffset(0.dp, (500).dp),
        scale = 0f,
        alpha = 1f,
        zIndex = 0f,
        aspectRatio = 1f,
    )

    private val standard = Props()

    private val destroyed = Props(
        offset = DpOffset(0.dp, (-500).dp),
        scale = 0f,
        alpha = 0f,
        zIndex = -1f,
        aspectRatio = 1f,
        rotation = 360f
    )

    override fun map(segment: TransitionModel.Segment<SpotlightModel.State<NavTarget>>): StateFlow<List<FrameModel<NavTarget>>> {
        val (_, targetState) = segment.navTransition

        return scroll.animateTo(
            segment,
            targetState.activeIndex,
            spring(
                stiffness = Spring.StiffnessVeryLow / 20,
//                dampingRatio = Spring.DampingRatioHighBouncy
            )
        )
    }

    override fun mapFrame(segment: TransitionModel.Segment<SpotlightModel.State<NavTarget>>): List<FrameModel<NavTarget>> {
        val (fromState, targetState) = segment.navTransition
        val fromProps = fromState.toProps()
        val targetProps = targetState.toProps()

        return targetProps.map { t1 ->
            val t0 = fromProps.find { it.element.id == t1.element.id }!!

            val alpha = lerpFloat(t0.props.alpha, t1.props.alpha, segment.progress)
            val scale = lerpFloat(t0.props.scale, t1.props.scale, segment.progress)
            val zIndex = lerpFloat(t0.props.zIndex, t1.props.zIndex, segment.progress)
            val aspectRatio = lerpFloat(t0.props.aspectRatio, t1.props.aspectRatio, segment.progress)
            val rotation = lerpFloat(t0.props.rotation, t1.props.rotation, segment.progress)
            val offset = lerpDpOffset(t0.props.offset, t1.props.offset, segment.progress)

            FrameModel(
                navElement = t1.element,
                modifier = Modifier
                    .offset(
                        x = offset.x,
                        y = offset.y
                    )
                    .zIndex(zIndex)
                    .aspectRatio(aspectRatio)
                    .scale(scale)
                    .rotate(rotation)
                    .alpha(alpha)
                ,
                progress = segment.progress
            )
        }
    }

    private fun <NavTarget> SpotlightModel.State<NavTarget>.toProps(): List<MatchedProps<NavTarget, Props>> {
        return positions.flatMapIndexed { index, position ->
            position.elements.map {
                val target = it.value.toProps()
                MatchedProps(
                    element = it.key,
                    props = Props(
                        offset = DpOffset(dpOffset(index).x, target.offset.y),
                        scale = target.scale,
                        alpha = target.alpha,
                        zIndex = target.zIndex,
                        rotation = target.rotation,
                        aspectRatio = target.aspectRatio,
                    )
                )
            }
        }
    }

    fun SpotlightModel.State.ElementState.toProps() =
        when (this) {
            CREATED -> created
            STANDARD -> standard
            DESTROYED -> destroyed
        }

    private fun dpOffset(
        index: Int
    ) = DpOffset(
        x = ((index - this.scroll.value) * width.value).dp,
        y = 0.dp
    )

    class Gestures<NavTarget>(
        transitionBounds: TransitionBounds,
        private val orientation: Orientation = Orientation.Horizontal, // TODO support RTL
    ) : GestureFactory<NavTarget, SpotlightModel.State<NavTarget>> {
        private val width = transitionBounds.widthPx
        private val height = transitionBounds.heightPx

        override fun createGesture(
            delta: Offset,
            density: Density
        ): Gesture<NavTarget, SpotlightModel.State<NavTarget>> {
            return when (orientation) {
                Orientation.Horizontal -> if (delta.x < 0) {
                    Gesture(
                        operation = Next(),
                        dragToProgress = { offset -> (offset.x / width) * -1 },
                        partial = { offset, progress -> offset.copy(x = progress * width * -1) }
                    )
                } else {
                    Gesture(
                        operation = Previous(),
                        dragToProgress = { offset -> (offset.x / width) },
                        partial = { offset, partial -> offset.copy(x = partial * width) }
                    )
                }
                Orientation.Vertical -> if (delta.y < 0) {
                    Gesture(
                        operation = Next(),
                        dragToProgress = { offset -> (offset.y / height) * -1 },
                        partial = { offset, partial -> offset.copy(y = partial * height * -1) }
                    )
                } else {
                    Gesture(
                        operation = Previous(),
                        dragToProgress = { offset -> (offset.y / height) },
                        partial = { offset, partial -> offset.copy(y = partial * height) }
                    )
                }
            }
        }
    }


    operator fun DpOffset.times(multiplier: Int) =
        DpOffset(x * multiplier, y * multiplier)

}

