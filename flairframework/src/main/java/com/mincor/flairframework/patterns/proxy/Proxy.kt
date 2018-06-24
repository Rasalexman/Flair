package com.mincor.flairframework.patterns.proxy

import com.mincor.flairframework.interfaces.IProxy
import com.mincor.flairframework.patterns.observer.Notifier

/**
 * Created by a.minkin on 21.11.2017.
 * @param data - stored data
 */
abstract class Proxy<out T>(override val data: T) : Notifier(), IProxy<T> {

    /**
     * Called by the Model when the Proxy is registered.
     */
    override fun onRegister() {}

    /**
     * Called by the Model when the Proxy is removed.
     */
    override fun onRemove() {}
}