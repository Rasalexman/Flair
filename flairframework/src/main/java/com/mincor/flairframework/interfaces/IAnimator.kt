package com.mincor.flairframework.interfaces

import android.animation.Animator

/**
 * Created by a.minkin on 24.11.2017.
 */
interface IAnimator {
    var to: IMediator?
    var from: IMediator?
    var duration: Long
    var isShow:Boolean
    var popLast:Boolean

    fun getAnimator():Animator
    fun playAnimation()
}