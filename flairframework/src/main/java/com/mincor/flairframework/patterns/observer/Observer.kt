package com.mincor.flairframework.patterns.observer

import com.mincor.flairframework.interfaces.INotificator
import com.mincor.flairframework.interfaces.IObserver


/**
 * Created by a.minkin on 21.11.2017.
 */
data class Observer(override var notify: INotificator?, override var context: Any?) : IObserver