package com.mincor.flairframework.interfaces

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
 * Compare an object to the notification appContext.
 *
 * @param object
 * the object to compare
 * @return boolean indicating if the object and the notification appContext are
 * the same
 */
fun IObserver.compareNotifyContext(`object`: Any): Boolean {
    return this.context === `object`
}

/**
 * Notify the interested object.
 *
 * @param notification
 * the `INotification` to pass to the interested
 * object's notification method.
 */
fun IObserver.notifyObserver(notification: INotification) {
    if(this.notify != null) this.notify!!(notification)
}