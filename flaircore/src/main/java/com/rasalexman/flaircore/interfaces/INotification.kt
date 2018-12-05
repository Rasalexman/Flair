package com.rasalexman.flaircore.interfaces

/**
 * Created by a.minkin on 21.11.2017.
 */
interface INotification {
    // the name of the notification core
    var name: String
    // the type of the notification core
    var type: String?
    // the body of the notification core
    var body: Any?
}

/**
 * Clear notification after it was pushed
 */
fun INotification.clear() {
    body = null
    type = null
}

/**
 * Get the string representation of the `Notification`
 * core.
 *
 * @return the string representation of the `Notification`
 * core.
 */
fun INotification.asString(): String {
    var result = "Notification Name: $name Body:"
    result += this.body?.let { it.toString()+" Type:" } ?:  "null Type:"
    result += type?:"null"
    return result
}