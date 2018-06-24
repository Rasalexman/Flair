package com.mincor.flairframework.interfaces

import android.app.Activity
import android.content.Context
import com.mincor.flairframework.core.FlairActivity
import com.mincor.flairframework.ext.flair

/**
 * Created by a.minkin on 21.11.2017.
 */

/**
 * The interface definition for a FlairFramework Notifier.
 *
 * <P>
 * <code>MacroCommand, Command, Mediator</code> and <code>Proxy</code> all
 * have a need to send <code>Notifications</code>.
 * </P>
 *
 * <P>
 * The <code>INotifier</code> interface provides a common method called
 * <code>sendNotification</code> that relieves implementation code of the
 * necessity to actually construct <code>Notifications</code>.
 * </P>
 *
 * <P>
 * The <code>Notifier</code> class, which all of the above mentioned classes
 * extend, also provides an initialized reference to the <code>Facade</code>
 * Singleton, which is required for the convienience method for sending
 * <code>Notifications</code>, but also eases implementation as these classes
 * have frequent <code>Facade</code> interactions and usually require access
 * to the facade anyway.
 * </P>
 *
 * @see  IFacade
 * @see  INotification
 */
interface INotifier : IMultitonKey {
    /**
     * Current Facade core reference
     */
    val facade: IFacade
}

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
inline fun <reified T : IProxy<*>, reified R : Any> INotifier.proxyModel(): Lazy<R> = lazy {
    if(facade.hasProxy<T>())  facade.retrieveProxy<T>().data as R else facade.registerProxy<T>().data as R
}

/**
 * Retrieve lazy proxy core or create new one if it does not has, by given generic class
 */
inline fun <reified T : IProxy<*>> INotifier.proxy(dataToHold: Map<String, Any>? = null): Lazy<T> = lazy {
    if(facade.hasProxy<T>()) facade.retrieveProxy<T>() else facade.registerProxy(dataToHold)
}


/**
 * Retrieve lazy mediator core by given generic class
 */
inline fun <reified T : IMediator> IMediator.mediator(mediatorName:String? = null): Lazy<T> = lazy {
    facade.retrieveMediator<T>(mediatorName)
}

/**
 * Main application appContext
 */
fun INotifier.appContext() : Context = facade.appContext

/**
 * Attached to facade single activity
 */
fun INotifier.activity() : FlairActivity = let{
    if (facade.view.currentActivity == null) throw RuntimeException("You need to set `currentActivity` for this core. Use `flair().attach()`")
    facade.view.currentActivity!!
}

/**
 * Register or Retrieve an instance of flair core
 * @param key
 * Core Name, used to get IFacade instance
 *
 * @param block
 * initialization function. This is a starting point to register Proxy/Mediators/Commands
 */
fun INotifier.flair(key: String? = null, block: FacadeInitializer? = null) = this.appContext().flair(key ?: this.multitonKey, block)