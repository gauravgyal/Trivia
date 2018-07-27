package com.gauravgoyal.trivia.utlis

import android.animation.Animator
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Button
import com.gauravgoyal.trivia.BuildConfig
import com.gauravgoyal.trivia.R
import com.gauravgoyal.trivia.model.Question


fun Any?.getQuestion() = Question("What is the capital of India?", "Delhi", "Bombay", "Chennai")


fun View.scaleUpFromCenter() {
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_up_from_center))
    visibility = View.VISIBLE
}

fun dualReveal(from: Button, to: Button, duration: Long = 500) {
    val startRadius = 0
    val endRadius = Math.hypot(from.width.toDouble(), from.height.toDouble()).toInt()
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        val anim = ViewAnimationUtils.createCircularReveal(to, from.width / 2, from.height / 2, startRadius.toFloat(), endRadius.toFloat())
        anim.interpolator = LinearInterpolator()
        anim.duration = duration
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                to.visibility = View.VISIBLE
                to.bringToFront()
            }

            override fun onAnimationEnd(animator: Animator) {
                from.visibility = View.INVISIBLE
                to.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        anim.start()
    } else {
        to.visibility = View.VISIBLE
    }
}

fun Any.logd(message: String) {
    if (BuildConfig.DEBUG) Log.d(this::class.java.simpleName, message)
}

