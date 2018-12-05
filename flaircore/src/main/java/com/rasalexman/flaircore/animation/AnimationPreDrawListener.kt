package com.rasalexman.flaircore.animation

import android.view.ViewTreeObserver

/**
 * Animation PreDraw Listener used as event handler when view added to container stage and it was initialized
 */
class AnimationPreDrawListener(private var toView: android.view.View?, private var startAnimationHandler: (() -> Unit)?) : ViewTreeObserver.OnPreDrawListener {
    /**
     * Does animation initialized and started
     */
    private var hasRun: Boolean = false

    /**
     * Wait until ui is drawing on screen
     */
    override fun onPreDraw(): Boolean = onReadyOrAborted()

    /**
     * When animation is ready to be played
     */
    private fun onReadyOrAborted(): Boolean {
        if (!hasRun) {
            hasRun = true
            toView?.let { view ->
                val observer = view.viewTreeObserver
                if (observer.isAlive) {
                    observer.removeOnPreDrawListener(this)
                    startAnimationHandler?.let { it() }
                }
            }
        }
        startAnimationHandler = null
        toView = null
        return true
    }
}