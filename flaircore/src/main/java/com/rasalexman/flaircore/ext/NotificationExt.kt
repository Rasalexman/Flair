package com.rasalexman.flaircore.ext

import com.rasalexman.flaircore.interfaces.INotification


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
    return buildString {
        append("Notification Name: $name Body:")
        append(body?.let { "$it Type:" } ?:  "null Type:")
        append(type?:"null")
    }
}