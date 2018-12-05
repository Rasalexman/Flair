package com.rasalexman.flaircore.animation

import com.rasalexman.flaircore.interfaces.IMediator

/**
 * Base linear back to animation without knowing of backstack navigation
 *
 * @param duration
 * time in ms
 */
class BackLinearAnimator(override var duration: Long = 500) : LinearAnimator() {

    override var to: IMediator? = null
    override var from: IMediator? = null

    override var isShow: Boolean = false
            get() = false

    override var popLast: Boolean = true
            get() = true


}