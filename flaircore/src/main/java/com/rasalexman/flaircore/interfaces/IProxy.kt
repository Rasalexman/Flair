package com.rasalexman.flaircore.interfaces

/**
 * Created by a.minkin on 21.11.2017.
 */
interface IProxy<T> : INotifier {

    /**
     * Storing data or service
     */
    var data:T

    /**
     * Called by the Model when the Proxy is registered.
     */
    fun onRegister()

    /**
     * Called by the Model when the Proxy is removed.
     */
    fun onRemove()
}