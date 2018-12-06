package com.rasalexman.flaircore.animation

/**
 * This is a base Next Mediator Animator
 *
 * @param duration
 * The time. it's how long animation will be playing in ms
 */
class NextLinearAnimator(override var duration: Long = 500,
                         private val localShow:Boolean = true,
                         private val localPopLast:Boolean = false
) : LinearAnimator() {

    override var isShow: Boolean = false
        get() = localShow

    override var popLast: Boolean = true
        get() = localPopLast
}