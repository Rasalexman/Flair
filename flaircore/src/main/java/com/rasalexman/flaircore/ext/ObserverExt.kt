package com.rasalexman.flaircore.ext

import com.rasalexman.flaircore.interfaces.INotification
import com.rasalexman.flaircore.interfaces.IObserver

/**
 * [INotification] callback function
 */
typealias INotificator = (notification: INotification) -> Unit

/**
 * Clear [IObserver] instance
 */
fun IObserver.clear(): Boolean {
    this.content = null
    this.notify = null
    return true
}

/**
 * Compare an compareObj to the notification appContext.
 *
 * @param compareObj - the compareObj to compare
 * @return boolean indicating if the compareObj and the notification appContext are
 * the same
 */
fun IObserver.compareNotifyContent(compareObj: Any): Boolean {
    return this.content === compareObj
}

/**
 * Notify the interested object.
 *
 * @param notification
 * the `INotification` to pass to the interested
 * object's notification method.
 */
fun IObserver.notifyObserver(notification: INotification) {
    this.notify?.invoke(notification)
    notification.clear()
}