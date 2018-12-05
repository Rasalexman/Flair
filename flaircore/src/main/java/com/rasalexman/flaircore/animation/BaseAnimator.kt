package com.rasalexman.flaircore.animation

import android.animation.Animator
import com.rasalexman.flairframework.interfaces.IAnimator
import com.rasalexman.flaircore.interfaces.hide

abstract class BaseAnimator : IAnimator {

    /**
     * Animation listener adapter
     */
    private var listenerAdapter: BaseAnimationListenerAdapter? = null

    /**
     * Prepare animation for play
     */
    protected open fun startAnimation() {
        val animator: Animator = getAnimator()
        animator.duration = duration
        listenerAdapter = BaseAnimationListenerAdapter(::finishAnimation)
        animator.addListener(listenerAdapter)
        to?.onAnimationStart(true)
        from?.onAnimationStart(false)
        animator.start()
    }

    /**
     * Clear the reference to given mediator instances
     */
    protected open fun finishAnimation(animation: Animator) {
        animation.removeAllListeners()
        animation.end()
        animation.cancel()

        from?.onAnimationFinish(false)
        from?.hide(null, popLast)
        to?.onAnimationFinish(true)
        clearAnimator()
    }

    /**
     * Clear all references
     */
    private fun clearAnimator() {
        listenerAdapter = null
        from = null
        to = null
    }
}