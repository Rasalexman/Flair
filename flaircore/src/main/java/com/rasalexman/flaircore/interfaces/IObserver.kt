package com.rasalexman.flaircore.interfaces

/**
 * [INotification] callback function
 */
typealias INotificator = (notification: INotification)->Unit
/**
 * Created by a.minkin on 21.11.2017.
 */
interface IObserver {
    /**
     * Main [IObserver] context
     */
    var context: Any?

    /**
     * [INotificator] instance
     */
    var notify: INotificator?
}

/**
 * Clear [IObserver] instance
 */
fun IObserver.clear():Boolean {
    this.context = null
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
fun IObserver.compareNotifyContext(compareObj: Any): Boolean {
    return this.context === compareObj
}

/**
 * Notify the interested object.
 *
 * @param notification
 * the `INotification` to pass to the interested
 * object's notification method.
 */
fun IObserver.notifyObserver(notification: INotification) {
    this.notify?.let {
        it(notification)
        notification.clear()
    }
}