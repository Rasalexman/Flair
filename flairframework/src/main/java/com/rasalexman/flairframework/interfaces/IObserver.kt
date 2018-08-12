package com.rasalexman.flairframework.interfaces

typealias INotificator = (notification: INotification)->Unit
/**
 * Created by a.minkin on 21.11.2017.
 */
interface IObserver {
    var context: Any?
    var notify: INotificator?
}

fun IObserver.clear():Boolean {
    this.context = null
    this.notify = null
    return true
}

/**
 * Compare an compareObj to the notification appContext.
 *
 * @param `compareObj`
 * the compareObj to compare
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
    }
}