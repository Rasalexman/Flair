package com.mincor.flairframework.interfaces

import com.mincor.flairframework.ext.className
import com.mincor.flairframework.ext.createInstance

/**
 * Created by a.minkin on 21.11.2017.
 */
interface IModel : IMultitonKey {
    val proxyMap: MutableMap<String, IProxy<*>>
}

/**
 * Check if a Proxy is registered.
 *
 * @return whether a Proxy is currently registered
 */
inline fun <reified T : IProxy<*>> IModel.hasProxy(): Boolean = proxyMap.containsKey(T::class.className())

/**
 * Retrieve an `IProxy` core from the Model.
 *
 * @return the `IProxy` core previously registered
 */
inline fun <reified T : IProxy<*>> IModel.retrieveProxy(): T = this.proxyMap[T::class.className()] as? T ?: registerProxy()

/**
 * Register an `IProxy` core with the `Model`.
 */
inline fun <reified T : IProxy<*>> IModel.registerProxy(dataToHold:Map<String, Any>? = null):T {
    val clazz = T::class
    val proxy = clazz.createInstance(dataToHold ?: hashMapOf())
    proxy.multitonKey = this.multitonKey
    this.proxyMap[clazz.className()] = proxy
    proxy.onRegister()
    return proxy
}

/**
 * Remove an `IProxy` core from the Model.
 */
inline fun <reified T : IProxy<*>> IModel.removeProxy(): T? {
    val clazz = T::class
    val clazzName = clazz.className()
    val proxy = this.proxyMap[clazzName] as? IProxy
    proxy?.let {
        this.proxyMap.remove(clazzName)
        it.onRemove()
    }
    return proxy as? T
}