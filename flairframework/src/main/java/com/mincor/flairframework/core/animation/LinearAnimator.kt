package com.mincor.flairframework.core.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewTreeObserver
import com.mincor.flairframework.interfaces.IAnimator
import com.mincor.flairframework.interfaces.IMediator
import com.mincor.flairframework.interfaces.hide

/**
 * Created by a.minkin on 24.11.2017.
 */
class LinearAnimator(override var from: IMediator? = null, override var to: IMediator? = null, override var isShow: Boolean = true, override var duration: Long = 500, override var popLast: Boolean = false) : IAnimator {

    override fun getAnimator(): Animator {
        val animatorSet = AnimatorSet()
        val fromView = from?.viewComponent
        val toView = to?.viewComponent

        if (isShow) {
            fromView?.let {
                animatorSet.play(ObjectAnimator.ofFloat<View>(it, View.TRANSLATION_X, -it.width.toFloat()))
            }
            toView?.let {
                animatorSet.play(ObjectAnimator.ofFloat<View>(it, View.TRANSLATION_X, it.width.toFloat(), 0f))
            }
        } else {
            fromView?.let {
                animatorSet.play(ObjectAnimator.ofFloat<View>(it, View.TRANSLATION_X, it.width.toFloat()))
            }
            toView?.let {
                val fromLeft = fromView?.translationX ?: 0f
                animatorSet.play(ObjectAnimator.ofFloat<View>(it, View.TRANSLATION_X, fromLeft - it.width.toFloat(), 0f))
            }
        }

        return animatorSet
    }

    override fun playAnimation() {
        to?.let {
            it.viewComponent?.viewTreeObserver?.addOnPreDrawListener(AnimationPreDrawListener(to!!.viewComponent))
        } ?: from?.let {
            startAnimation()
        }
    }

    private fun startAnimation() {
        val animator: Animator = getAnimator()
        if (duration > 0) {
            animator.duration = duration
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationCancel(animation: Animator) {
                clearAnimation(animation)
            }

            override fun onAnimationEnd(animation: Animator) {
                from?.hide(null, popLast)
                clearAnimation(animation)
            }

            private fun clearAnimation(animation: Animator) {
                from = null
                to = null
                animation.removeAllListeners()
                animation.end()
                animation.cancel()
            }
        })
        animator.start()
    }

    inner class AnimationPreDrawListener(private var toView: android.view.View?) : ViewTreeObserver.OnPreDrawListener {
        private var hasRun: Boolean = false
        override fun onPreDraw(): Boolean {
            onReadyOrAborted()
            return true
        }


        /**
         * When animation is ready to be played
         */
        private fun onReadyOrAborted() {
            if (!hasRun) {
                hasRun = true
                toView?.let {
                    val observer = it.viewTreeObserver
                    if (observer.isAlive) {
                        observer.removeOnPreDrawListener(this)
                        startAnimation()
                    }
                }
            }
            toView = null
        }
    }
}