package com.rasalexman.flairframework.core.animation

import com.rasalexman.flairframework.interfaces.IMediator

/**
 * This is a base Next Mediator Animator
 *
 * @param duration
 * The time. it's how long animation will be playing in ms
 */
class NextLinearAnimator(override var duration: Long = 500) : LinearAnimator() {

    override var to: IMediator? = null
    override var from: IMediator? = null

    override var isShow: Boolean = false
        get() = true

    override var popLast: Boolean = true
        get() = false


}