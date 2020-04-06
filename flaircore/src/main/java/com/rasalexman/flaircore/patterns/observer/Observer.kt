package com.rasalexman.flaircore.patterns.observer

import com.rasalexman.flaircore.interfaces.INotificator
import com.rasalexman.flaircore.interfaces.IObserver


/**
 * Created by a.minkin on 21.11.2017.
 *
 * Main Observer class
 *
 * @param context - data for compare
 * @param notify - INotifier instance
 */
data class Observer(
        override var context: Any?,
        override var notify: INotificator?
) : IObserver