package com.bumble.appyx.transitionmodel

import DefaultAnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.bumble.appyx.combineState
import com.bumble.appyx.interactions.Logger
import com.bumble.appyx.interactions.core.Element
import com.bumble.appyx.interactions.core.model.progress.Draggable
import com.bumble.appyx.interactions.core.model.transition.Segment
import com.bumble.appyx.interactions.core.model.transition.Update
import com.bumble.appyx.interactions.core.ui.MotionController
import com.bumble.appyx.interactions.core.ui.context.UiContext
import com.bumble.appyx.interactions.core.ui.gesture.DraggableMapping
import com.bumble.appyx.interactions.core.ui.math.lerpFloat
import com.bumble.appyx.interactions.core.ui.output.ElementUiModel
import com.bumble.appyx.interactions.core.ui.property.MotionProperty
import com.bumble.appyx.interactions.core.ui.state.BaseMutableUiState
import com.bumble.appyx.interactions.core.ui.state.UiMapping
import com.bumble.appyx.withPrevious
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseMotionController<InteractionTarget : Any, ModelState, MutableUiState, TargetUiState>(
    private val uiContext: UiContext,
    protected val defaultAnimationSpec: SpringSpec<Float> = DefaultAnimationSpec,
) : MotionController<InteractionTarget, ModelState> where MutableUiState : BaseMutableUiState<MutableUiState, TargetUiState> {

    protected lateinit var draggable: Draggable

    override fun init(draggable: Draggable) {
        this.draggable = draggable
    }

    open val geometryMappings: List<Pair<(ModelState) -> Float, MotionProperty<Float, AnimationVector1D>>> =
        emptyList()

    private val coroutineScope = uiContext.coroutineScope
    private val mutableUiStateCache: MutableMap<String, MutableUiState> = mutableMapOf()
    private val animations: MutableMap<String, Boolean> = mutableMapOf()
    private val geometryModeAnimatingState: StateFlow<Boolean>
        get() = if (geometryMappings.isNotEmpty()) {
            combineState(
                geometryMappings.map { it.second.isAnimating },
                uiContext.coroutineScope
            ) { values ->
                values.any { it }
            }
        } else {
            MutableStateFlow(false)
        }
    private val updateModeAnimatingState = MutableStateFlow(false)
    private val isAnimatingState: StateFlow<Boolean>
        get() = updateModeAnimatingState.combineState(
            geometryModeAnimatingState,
            uiContext.coroutineScope
        ) { isGeometryAnimating, isUpdateAnimating ->
            isGeometryAnimating || isUpdateAnimating
        }

    protected var currentSpringSpec: SpringSpec<Float> = defaultAnimationSpec

    private val _finishedAnimations = MutableSharedFlow<Element<InteractionTarget>>()
    override val finishedAnimations: Flow<Element<InteractionTarget>> = _finishedAnimations
    override fun overrideAnimationSpec(springSpec: SpringSpec<Float>) {
        currentSpringSpec = springSpec
    }

    final override fun isAnimating(): StateFlow<Boolean> =
        isAnimatingState

    protected abstract fun ModelState.toUiTargets(): List<UiMapping<InteractionTarget, TargetUiState>>

    protected abstract fun mutableUiStateFor(
        uiContext: UiContext,
        uiMapping: UiMapping<*, TargetUiState>
    ): MutableUiState

    private fun draggableMutableUiStateFor(uiContext: UiContext, uiMapping: UiMapping<*, TargetUiState>): MutableUiState =
        mutableUiStateFor(uiContext, uiMapping).apply {
            makeDraggable(uiMapping.element, draggable)
        }

    override fun mapUpdate(
        update: Update<ModelState>
    ): List<ElementUiModel<InteractionTarget>> {
        val matchedTargetUiStates = update.currentTargetState.toUiTargets()

        coroutineScope.launch {
            updateGeometry(update)
        }

        // TODO: use a map instead of find
        return matchedTargetUiStates.map { t1 ->
            val mutableUiState = mutableUiStateCache.getOrPut(t1.element.id) {
                draggableMutableUiStateFor(uiContext, t1)
            }
            ElementUiModel(
                element = t1.element,
                visibleState = mutableUiState.isVisible,
                animationContainer = @Composable {
                    observeElementAnimationChanges(mutableUiState, t1)
                    manageAnimations(mutableUiState, t1, update)
                },
                modifier = mutableUiState.modifier,
                progress = MutableStateFlow(1f),
            )
        }
    }

    @Composable
    private fun manageAnimations(
        mutableUiState: MutableUiState,
        uiMapping: UiMapping<InteractionTarget, TargetUiState>,
        update: Update<ModelState>
    ) {
        LaunchedEffect(update, this) {
            // make sure to use scope created by Launched effect as this scope should be cancelled
            // when associated FrameModel cease to exist
            launch {
                if (update.animate) {
                    mutableUiState.animateTo(
                        scope = this,
                        target = uiMapping.targetUiState,
                        springSpec = currentSpringSpec,
                    )
                } else {
                    mutableUiState.snapTo(this, uiMapping.targetUiState)
                }
            }
        }
    }

    @Composable
    private fun observeElementAnimationChanges(
        mutableUiState: MutableUiState,
        uiMapping: UiMapping<InteractionTarget, TargetUiState>
    ) {
        LaunchedEffect(this) {
            // make sure to use scope created by Launched effect as this scope should be cancelled
            // when associated FrameModel cease to exist
            launch {
                mutableUiState.isAnimating
                    .distinctUntilChanged()
                    .withPrevious()
                    .collect { values ->
                        val previous = values.previous ?: return@collect
                        val current = values.current
                        if (current && !previous) {
                            // animation started
                            animations[uiMapping.element.id] = true
                            updateModeAnimatingState.update { true }
                            Logger.log(
                                this@BaseMotionController.javaClass.simpleName,
                                "animation for element ${uiMapping.element.id} is started"
                            )
                        } else {
                            // animation finished
                            _finishedAnimations.emit(uiMapping.element)
                            animations[uiMapping.element.id] = false
                            updateModeAnimatingState.update { animations.any { it.value } }
                            Logger.log(
                                this@BaseMotionController.javaClass.simpleName,
                                "animation for element ${uiMapping.element.id} is finished"
                            )
                        }
                    }
            }
        }
    }

    private suspend fun updateGeometry(update: Update<ModelState>) {
        geometryMappings.forEach { (fieldOfState, geometry) ->
            val targetValue = fieldOfState(update.currentTargetState)
            geometry.animateTo(
                targetValue,
                spring(
                    stiffness = currentSpringSpec.stiffness,
                    dampingRatio = currentSpringSpec.dampingRatio
                )
            ) {
                Logger.log(
                    this@BaseMotionController.javaClass.simpleName,
                    "Geometry animateTo (Update) – $targetValue"
                )
            }
        }
    }

    override fun mapSegment(
        segment: Segment<ModelState>,
        segmentProgress: Flow<Float>,
        initialProgress: Float
    ): List<ElementUiModel<InteractionTarget>> {
        val (fromState, targetState) = segment.stateTransition
        val fromTargetUiState = fromState.toUiTargets()
        val toTargetUiState = targetState.toUiTargets()

        coroutineScope.launch {
            segmentProgress.collect {
                updateGeometry(segment, it)
            }
        }

        // TODO: use a map instead of find
        return toTargetUiState.map { t1 ->
            val t0 = fromTargetUiState.find { it.element.id == t1.element.id }!!
            val mutableUiState = mutableUiStateCache.getOrPut(t1.element.id) {
                draggableMutableUiStateFor(uiContext, t0)
            }
            // Synchronously, immediately apply current interpolated value before the new mutable state
            // reaches composition. This is to avoid jumping between default & current value.
            mutableUiState.lerpTo(coroutineScope, t0.targetUiState, t1.targetUiState, initialProgress)

            ElementUiModel(
                element = t1.element,
                visibleState = mutableUiState.isVisible,
                animationContainer = @Composable {
                    interpolateUiState(segmentProgress, mutableUiState, t0, t1, initialProgress)
                },
                modifier = mutableUiState.modifier,
                progress = segmentProgress,
            )
        }
    }

    @Composable
    private fun interpolateUiState(
        segmentProgress: Flow<Float>,
        mutableUiState: MutableUiState,
        from: UiMapping<InteractionTarget, TargetUiState>,
        to: UiMapping<InteractionTarget, TargetUiState>,
        initialProgress: Float
    ) {
        val progress by segmentProgress.collectAsState(initialProgress)
        LaunchedEffect(progress) {
            mutableUiState.lerpTo(coroutineScope, from.targetUiState, to.targetUiState, progress)
        }
    }

    private suspend fun updateGeometry(
        segment: Segment<ModelState>,
        segmentProgress: Float
    ) {
        geometryMappings.forEach { (fieldOfState, geometry) ->
            val (behaviour, targetValue) = geometryTargetValue(
                segment,
                segmentProgress,
                fieldOfState
            )

            when (behaviour) {
                GeometryBehaviour.SNAP -> {
                    geometry.snapTo(targetValue)
                    Logger.log(
                        this@BaseMotionController.javaClass.simpleName,
                        "Geometry snapTo (Segment): $targetValue"
                    )
                }

                GeometryBehaviour.ANIMATE -> {
                    if (geometry.value != targetValue) {
                        geometry.animateTo(
                            targetValue, spring(
                                stiffness = currentSpringSpec.stiffness,
                                dampingRatio = currentSpringSpec.dampingRatio
                            )
                        ) {
                            Logger.log(
                                this@BaseMotionController.javaClass.simpleName,
                                "Geometry animateTo (Segment) – ${geometry.value} -> $targetValue"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun geometryTargetValue(
        segment: Segment<ModelState>,
        segmentProgress: Float,
        fieldOfState: (ModelState) -> Float
    ): Pair<GeometryBehaviour, Float> {
        val fromValue = fieldOfState(segment.fromState)
        val targetValue = fieldOfState(segment.targetState)

        // If the geometry value was inserted via a GEOMETRY operation mode, then it was applied both to the
        // start and end values (see [BaseTransitionModel.updateGeometry()], and they should be the same.
        // This means that even though we have a Segment, the interpolation is working on some other part of
        // the ModelState, not the geometry, and we should still consider the geometry value as a separate concern,
        // that we need to animate.
        return if (fromValue == targetValue) GeometryBehaviour.ANIMATE to targetValue
        // If the geometry value was added via a KEYFRAME operation mode, then the relevant value
        // will be different for the targetValue.
        // This means that the Segment was specifically created to interpolate the geometry value (probably a gesture)
        // and that it's important to follow the interpolation by snapping.
        else GeometryBehaviour.SNAP to lerpFloat(fromValue, targetValue, segmentProgress)
    }

    private enum class GeometryBehaviour {
        SNAP, ANIMATE
    }

}
