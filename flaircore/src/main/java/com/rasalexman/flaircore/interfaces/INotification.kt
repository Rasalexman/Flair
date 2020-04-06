package com.rasalexman.flaircore.interfaces

/**
 * Created by a.minkin on 21.11.2017.
 */
interface INotification {
    /**
     * the name of the notification core
     */
    var name: String

    /**
     * the type of the notification core
     */
    var type: String?

    /**
     * the body of the notification core
     */
    var body: Any?
}