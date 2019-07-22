package com.rasalexman.flaircore.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter

/**
 * Base animation Listener adapter for handle when animation is comlete or cancel
 *
 * @param finishAnimationHandler - animation for finish
 */
class BaseAnimationListenerAdapter(
        private var finishAnimationHandler: ((Animator) -> Unit)? = null
) : AnimatorListenerAdapter() {
    /**
     * When animation cancel we gonna clear it
     */
    override fun onAnimationCancel(animation: Animator) {
        finishAnimationHandler?.let { it(animation) }
        finishAnimationHandler = null
        super.onAnimationCancel(animation)
    }

    /**
     * When animation ends we hide mediator with given params and clear animation
     */
    override fun onAnimationEnd(animation: Animator) {
        onAnimationCancel(animation)
        super.onAnimationEnd(animation)
    }
}