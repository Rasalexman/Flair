package com.rasalexman.flaircore.animation

import android.animation.Animator
import com.rasalexman.flaircore.interfaces.IAnimator
import com.rasalexman.flaircore.interfaces.IMediator
import com.rasalexman.flaircore.interfaces.hide

/**
 * Base Animation class to extend
 */
abstract class BaseAnimator : IAnimator {

    /**
     * Mediator to from animation
     */
    override var from: IMediator? = null

    /**
     * Mediator to push to
     */
    override var to: IMediator? = null

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
     * Play animation
     */
    override fun playAnimation() {
        to?.let {
            it.viewComponent?.viewTreeObserver?.addOnPreDrawListener(AnimationPreDrawListener(to!!.viewComponent, ::startAnimation))
        } ?: from?.let {
            startAnimation()
        }
    }

    /**
     * Clear all references
     */
    open protected fun clearAnimator() {
        listenerAdapter = null
        from = null
        to = null
    }
}