package com.rasalexman.flairframework.patterns.observer

import com.rasalexman.flairframework.interfaces.INotificator
import com.rasalexman.flairframework.interfaces.IObserver


/**
 * Created by a.minkin on 21.11.2017.
 */
data class Observer(override var notify: INotificator?, override var context: Any?) : IObserver