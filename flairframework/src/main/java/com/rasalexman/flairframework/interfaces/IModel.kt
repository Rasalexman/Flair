package com.rasalexman.flairframework.interfaces

import android.util.ArrayMap
import com.rasalexman.flairframework.ext.className
import com.rasalexman.flairframework.ext.createInstance
import com.rasalexman.flairframework.ext.injectInConstructor

/**
 * Created by a.minkin on 21.11.2017.
 */
interface IModel : IMultitonKey {
    val proxyMap: ArrayMap<String, IProxy<*>>
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
inline fun <reified T : IProxy<*>> IModel.retrieveProxy(params:List<Any>? = null): T = this.proxyMap[T::class.className()]?.injectInConstructor(params) as? T ?: registerProxy(params)


/**
 * Register an `IProxy` core with the `Model`.
 */
inline fun <reified T : IProxy<*>> IModel.registerProxy(consParams:List<Any>? = null):T {
    val clazz = T::class
    val proxy = clazz.createInstance(consParams)
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