package com.rasalexman.flaircore.ext

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.rasalexman.flaircore.interfaces.INotifier
import com.rasalexman.flaircore.interfaces.IProxy


/**
 * Send a `INotification`.
 *
 * <P>
 * Convenience method to prevent having to construct new notification
 * instances in our implementation code.
</P> *
 *
 * @param notificationName
 * the name of the notification to send
 * @param body
 * the body of the notification (optional)
 * @param type
 * the type of the notification (optional)
 */
fun INotifier.sendNotification(notificationName: String, body: Any? = null, type: String? = null) {
    facade.sendNotification(notificationName, body, type)
}

/**
 * Retrieve lazy proxyModel data by given generic class
 */
inline fun <reified T : IProxy<*>, reified R : Any> INotifier.proxyLazyModel(): Lazy<R> = lazy {
    proxyModel<T, R>()
}

/**
 * Retrieve proxyModel data by given generic class
 */
inline fun <reified T : IProxy<*>, reified R : Any> INotifier.proxyModel(): R = facade.retrieveProxy<T>().data as R

/**
 * Retrieve lazy proxy core or create new one if it does not has, by given generic class
 */
inline fun <reified T : IProxy<*>> INotifier.proxyLazy(noinline proxyBuilder: (() -> T)? = null): Lazy<T> = lazy {
    if (facade.hasProxy<T>()) {
        facade.retrieveProxy<T>()
    } else {
        proxyBuilder?.let {builder ->
            this.facade.registerProxy(builder)
            facade.retrieveProxy<T>()
        } ?: throw RuntimeException("You need to register proxy instance first or set proxyBuilder")
    }
}

/**
 * Retrieve proxy core or create new one if it does not has, by given generic class
 */
inline fun <reified T : IProxy<*>> INotifier.proxy(): T = facade.retrieveProxy()

/**
 * Main application appContext
 */
val INotifier.appContext: Context
    get() = facade.appContext.get() ?: throw IllegalAccessException("There is no context for this core. Map the core first by calling Context.flair()")

/**
 * Attached to facade single activity
 */
val INotifier.activity: FragmentActivity
    get() = facade.view.currentActivity?.get()
            ?: throw RuntimeException("You need to set `currentActivity` for this core. Use `flair().attach()`")

/**
 * Register or Retrieve an instance of flair core
 * @param key
 * Core Name, used to get IFacade instance
 *
 * @param block
 * initialization function. This is a starting point to register Proxy/Mediators/Commands
 */
fun INotifier.flair(key: String? = null, block: FacadeInitializer? = null) = appContext.flair(key
        ?: this.multitonKey, block)