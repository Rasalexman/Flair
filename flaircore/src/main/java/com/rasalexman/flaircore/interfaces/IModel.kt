package com.rasalexman.flaircore.interfaces

import androidx.collection.ArrayMap

/**
 * Created by a.minkin on 21.11.2017.
 */
interface IModel : IMultitonKey {
    /**
     * Main [IProxy] storage for [IFacade] core
     */
    val proxyMap: ArrayMap<String, IProxy<*>>
}

/**
 * Check if a Proxy is registered.
 *
 * @return whether a Proxy is currently registered
 */
inline fun <reified T : IProxy<*>> IModel.hasProxy(): Boolean = proxyMap.containsKey(T::class.toString())

/**
 * Retrieve an `IProxy` core from the Model.
 *
 * @return the `IProxy` core previously registered
 */
inline fun <reified T : IProxy<*>> IModel.retrieveProxy(): T = this.proxyMap[T::class.toString()] as? T
        ?: throw RuntimeException("There is no proxy with given class name ${T::class}. Register it first")


/**
 * Register an `IProxy` core with the `Model`.
 *
 * @param proxyBuilder builder function
 * @return instance of registered [IProxy]
 */
inline fun <reified T : IProxy<*>> IModel.registerProxy(proxyBuilder: () -> T) {
    val clazz = T::class.toString()
    this.proxyMap.getOrPut(clazz) {
        val proxy = proxyBuilder()
        proxy.multitonKey = this.multitonKey
        proxy.onRegister()
        proxy
    }
}

/**
 * Remove an `IProxy` core from the Model.
 */
inline fun <reified T : IProxy<*>> IModel.removeProxy(): Boolean {
    val className = T::class.toString()
    val proxy = this.proxyMap.remove(className)
    proxy?.onRemove()
    return proxy != null
}