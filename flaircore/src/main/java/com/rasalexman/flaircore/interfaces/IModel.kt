package com.rasalexman.flaircore.interfaces

/**
 * Created by a.minkin on 21.11.2017.
 */
interface IModel : IMultitonKey {
    val proxyMap: HashMap<String, IProxy<*>>
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
 */
inline fun <reified T : IProxy<*>> IModel.registerProxy(proxyBuilder:()->T):T {
    val clazz = T::class
    val proxy = proxyBuilder()
    proxy.multitonKey = this.multitonKey
    this.proxyMap[clazz.toString()] = proxy
    proxy.onRegister()
    return proxy
}

/**
 * Remove an `IProxy` core from the Model.
 */
inline fun <reified T : IProxy<*>> IModel.removeProxy(): T? {
    val className = T::class.toString()
    val proxy = this.proxyMap[className] as? IProxy
    proxy?.let {
        this.proxyMap.remove(className)
        it.onRemove()
    }
    return proxy as? T
}