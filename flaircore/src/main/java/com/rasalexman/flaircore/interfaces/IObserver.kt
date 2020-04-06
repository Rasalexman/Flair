package com.rasalexman.flaircore.interfaces

import com.rasalexman.flaircore.ext.INotificator

/**
 * Created by a.minkin on 21.11.2017.
 */
interface IObserver {
    /**
     * Main [IObserver] context
     */
    var content: Any?

    /**
     * [INotificator] instance
     */
    var notify: INotificator?
}