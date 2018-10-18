package com.rasalexman.flairframework.core.animation

import com.rasalexman.flairframework.interfaces.IMediator

class NextLinearAnimator : LinearAnimator() {

    override var to: IMediator? = null
    override var from: IMediator? = null

    override var duration: Long = 500

    override var isShow: Boolean = false
        get() = true

    override var popLast: Boolean = true
        get() = false


}