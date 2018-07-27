package com.gauravgoyal.trivia.widget

import android.animation.TimeInterpolator
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.constraint.Group
import android.support.transition.Fade
import android.support.transition.Transition
import android.support.transition.TransitionManager
import android.support.transition.TransitionSet
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.util.Log
import android.view.View


class HierarchyAnimationGroup : Group {

    private var partialDelay = DEFAULT_PARTIAL_TRANSITION_DELAY
    private var partialDuration = DEFAULT_PARTIAL_DURATION
    private var partialInterpolator = DEFAULT_PARTIAL_INTERPOLATOR
    private var partialTransitionFactory = defaultPartialTransitionFactory
    private var onPreparedListener = defaultOnPreparedListener

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    private fun prepareHierarchyTransition(isShowing: Boolean): Transition {
        val staggeredTransition = TransitionSet()
        val nonZeroIds = mIds.filter { it != 0 }.toIntArray()
        for (iteration in nonZeroIds.indices) {
            val id = nonZeroIds[iteration]
            val basePartialTransition = preparePartialTransition(isShowing, id, iteration)
            addTransition(
                    basePartialTransition, staggeredTransition, id, iteration)
        }
        return onStaggeredTransitionReady(staggeredTransition, isShowing)
    }


    private fun addTransition(basePartialTransition: Transition,
                              staggeredTransition: TransitionSet,
                              viewId: Int, indexInTransition: Int) {
        val partialTransition = applyParams(basePartialTransition, viewId, indexInTransition)
        staggeredTransition.addTransition(partialTransition)
    }


    private fun applyParams(partialTransition: Transition,
                            viewId: Int, indexInTransition: Int) =
            partialTransition.apply {
                startDelay = (indexInTransition * partialDelay).toLong()
                addTarget(viewId)
            }


    private fun preparePartialTransition(isShowing: Boolean, id: Int, indexInTransition: Int) = partialTransitionFactory
            .createPartialTransition(isShowing, id, indexInTransition)
            .setDuration(partialDuration.toLong())
            .setInterpolator(partialInterpolator)


    private fun onStaggeredTransitionReady(transition: TransitionSet, isShowing: Boolean) =
            onPreparedListener.onTransitionPrepared(transition, isShowing)


    fun show() {
        val parent = this.parent
        if (parent is ConstraintLayout) {
            val transition = prepareHierarchyTransition(true)
            TransitionManager.beginDelayedTransition(parent, transition)
            visibility = View.VISIBLE
        } else
            Log.d(TAG, "use HierarhcyAnimationGroup inside ConstraintLayout")
    }

    private interface PartialTransitionFactory {
        fun createPartialTransition(show: Boolean,
                                    viewId: Int,
                                    indexInTransition: Int): Transition
    }

    private interface OnTransitionPreparedListener {
        fun onTransitionPrepared(transitionSet: TransitionSet,
                                 show: Boolean): TransitionSet
    }

    companion object {
        private const val TAG = "HierarchyAnimationGroup"
        private const val DEFAULT_PARTIAL_DURATION = 500
        private const val DEFAULT_PARTIAL_TRANSITION_DELAY = 100
        private val DEFAULT_PARTIAL_INTERPOLATOR: TimeInterpolator = FastOutSlowInInterpolator()
        private var defaultPartialTransitionFactory: PartialTransitionFactory = object : PartialTransitionFactory {
            override fun createPartialTransition(show: Boolean, viewId: Int, indexInTransition: Int) = Fade()
        }
        private var defaultOnPreparedListener: OnTransitionPreparedListener = object : OnTransitionPreparedListener {
            override fun onTransitionPrepared(transitionSet: TransitionSet, show: Boolean) = transitionSet

        }
    }
}
