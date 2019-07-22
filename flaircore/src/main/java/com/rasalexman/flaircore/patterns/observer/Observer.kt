package com.rasalexman.flaircore.patterns.observer

import com.rasalexman.flaircore.interfaces.INotificator
import com.rasalexman.flaircore.interfaces.IObserver


/**
 * Created by a.minkin on 21.11.2017.
 *
 * Main Observer class
 *
 * @param notify - INotifier instance
 * @param context - data for compare
 */
data class Observer(
        override var notify: INotificator?,
        override var context: Any?
) : IObserver