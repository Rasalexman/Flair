package com.rasalexman.flaircore.patterns.proxy

import com.rasalexman.flaircore.interfaces.IProxy
import com.rasalexman.flaircore.patterns.observer.Notifier

/**
 * Created by a.minkin on 21.11.2017.
 * @param data - data for store into proxy object
 */
abstract class Proxy<T>(override var data: T? = null) : Notifier(), IProxy<T?> {

    /**
     * Called by the Model when the Proxy is registered.
     */
    override fun onRegister() {}

    /**
     * Called by the Model when the Proxy is removed.
     */
    override fun onRemove() {}
}