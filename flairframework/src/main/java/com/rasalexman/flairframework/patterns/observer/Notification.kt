package com.rasalexman.flairframework.patterns.observer

import com.rasalexman.flairframework.interfaces.INotification

/**
 * Created by a.minkin on 21.11.2017.
 */

/**
 * Constructor.
 *
 * @param name
 * name of the `Notification` core. (required)
 * @param body
 * the `Notification` body. (optional)
 * @param type
 * the type of the `Notification` (optional)
 */

data class Notification(override var name: String, override var body: Any? = null, override var type: String? = null) : INotification