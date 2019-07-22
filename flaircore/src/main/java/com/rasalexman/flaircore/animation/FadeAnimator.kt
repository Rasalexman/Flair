package com.rasalexman.flaircore.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

/**
 * Base Fade Animator
 */
open class FadeAnimator : BaseAnimator() {
    /**
     * Does we need to use it for show mediator animation
     */
    override var isShow: Boolean = true

    /**
     * Duration of current animation
     */
    override var duration: Long = 500

    /**
     * Does we need to pop last mediator from backstack
     */
    override var popLast: Boolean = false

    /**
     * Get current animation
     */
    override fun getAnimator(): Animator {
        val animatorSet = AnimatorSet()
        val fromView = from?.viewComponent
        val toView = to?.viewComponent

        fromView?.let {
            animatorSet.play(ObjectAnimator.ofFloat<View>(it, View.ALPHA, 0f))
        }
        toView?.let {
            animatorSet.play(ObjectAnimator.ofFloat<View>(it, View.ALPHA, 0f, 1f))
        }

        return animatorSet
    }

    override fun clearAnimator() {
        from?.viewComponent?.alpha = 1f
        super.clearAnimator()
    }
}