package com.rasalexman.flaircore.animation

/**
 * Base linear back to animation without knowing of backstack navigation
 *
 * @param duration
 * time in ms
 */
class BackLinearAnimator(override var duration: Long = 500,
                         private val localShow:Boolean = false,
                         private val localPopLast:Boolean = true
) : LinearAnimator() {

    override var isShow: Boolean = false
            get() = localShow

    override var popLast: Boolean = true
            get() = localPopLast
}