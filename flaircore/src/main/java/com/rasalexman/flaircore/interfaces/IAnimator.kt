package com.rasalexman.flairframework.interfaces

import android.animation.Animator
import com.rasalexman.flaircore.interfaces.IMediator

/**
 * Created by a.minkin on 24.11.2017.
 */
interface IAnimator {
    /**
     * IMediator we want to animate to
     */
    var to: IMediator?
    /**
     * The IMedaitor instance from we animate
     */
    var from: IMediator?
    /**
     * Animation duration
     */
    var duration: Long
    /**
     * This flag indicates if we going to show or pop mediator
     */
    var isShow:Boolean
    /**
     * Remove last mediator from backstack and clear there view
     */
    var popLast:Boolean

    /**
     * Get the animation for transitions
     */
    fun getAnimator():Animator

    /**
     * Start playing current animation
     */
    fun playAnimation()
}