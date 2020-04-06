package com.rasalexman.flaircore.animation

/**
 * This is a base Next Mediator Animator
 *
 * @param duration
 * The time. it's how long animation will be playing in ms
 *
 * @param localShow - change local params
 *
 * @param localPopLast - change local params
 */
class NextLinearAnimator(
        override var duration: Long = 500,
        private val localShow: Boolean = true,
        private val localPopLast: Boolean = false
) : LinearAnimator() {

    /**
     * Mediator to from animation
     */
    override var isShow: Boolean = false
        get() = localShow

    /**
     * Mediator to push to
     */
    override var popLast: Boolean = true
        get() = localPopLast
}