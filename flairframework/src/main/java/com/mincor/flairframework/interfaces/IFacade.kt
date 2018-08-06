package com.mincor.flairframework.interfaces

import android.content.Context
import android.view.ViewGroup
import com.mincor.flairframework.core.FlairActivity
import com.mincor.flairframework.core.animation.LinearAnimator
import com.mincor.flairframework.core.controller.Controller
import com.mincor.flairframework.core.model.Model
import com.mincor.flairframework.core.view.View
import com.mincor.flairframework.ext.className
import com.mincor.flairframework.patterns.facade.Facade
import com.mincor.flairframework.patterns.observer.Notification
import com.mincor.flairframework.patterns.observer.Observer

typealias FacadeInitializer = IFacade.() -> Unit

/**
 * Created by a.minkin on 21.11.2017.
 */
interface IFacade : INotifier {

    val appContext: Context
    val controller: IController
    val model: IModel
    val view: IView

    /***
     * Attach main Activity class to current Facade Core
     * @param activity
     * Activity for attachment and life-cyrcle handled
     *
     * @param container
     * Container for add/remove ui
     */
    fun attach(activity: FlairActivity, container: ViewGroup? = null): IFacade

    companion object : IMapper<IFacade> {
        const val DEFAULT_KEY = "DEFAULT_FACADE"
        override val instanceMap: MutableMap<String, IFacade> = mutableMapOf()

        /**
         * Facade Multiton Factory method.
         *
         * @return the Multiton core of the Facade
         */
        fun core(key: String = DEFAULT_KEY, context: Context? = null, init: FacadeInitializer? = null): IFacade = instance(key) {
            if (context == null) throw RuntimeException("You need to specified `context` for this core")
            Facade(key, context, init)
        }

        /**
         * Check if a Core is registered or not.
         *
         * @param key the multiton key for the Core in question
         * @return whether a Core is registered with the given `key`.
         */
        @Synchronized
        fun hasCore(key: String = DEFAULT_KEY): Boolean = instanceMap.containsKey(key)

        /**
         * Remove a Core
         * @param key of the Core to remove
         */
        @Synchronized
        fun removeCore(key: String = DEFAULT_KEY) {
            // remove the model, view, controller
            // and facade instances for this key
            Model.removeModel(key)
            View.removeView(key)
            Controller.removeController(key)
            instanceMap.remove(key)
        }
    }
}

/////---------- INLINE SECTION -----///
/**
 * Retrieve an `IMediator` core from the `View`.
 *
 * @param mediatorName
 * Mediator name to retrieve
 *
 * @return the T : `IMediator` previously registered with the given
 * `mediatorName`.
 */
inline fun <reified T : IMediator> IFacade.retrieveMediator(mediatorName: String? = null): T = this.view.retrieveMediator(mediatorName)

/**
 * Register an `IMediator` with the `View` core.
 */
inline fun <reified T : IMediator> IFacade.registerMediator(mediatorName: String? = null) {
    this.view.registerMediator<T>(mediatorName)
}

/**
 * Remove an `IMediator` from the `View` core.
 */
inline fun <reified T : IMediator> IFacade.removeMediator(mediatorName: String? = null): T? = this.view.removeMediator<T>(mediatorName) as? T

/**
 * Show last added IMediator from backstack. If there is no mediator in backstack show the one passed by generic type class
 */
inline fun <reified T : IMediator> IFacade.showLastOrExistMediator(animation: IAnimator? = null) {
    view.showLastOrExistMediator<T>(animation)
}

/**
 * Check if a IMediator is registered or not
 */
inline fun <reified T : IMediator> IFacade.hasMediator(mediatorName: String? = null): Boolean = view.hasMediator<T>(mediatorName)

/**
 * Show current selected mediator
 *
 * @param mapName
 * This is a current mediator name to put in backstack
 *
 * @param popLast
 * flag that indicates need to remove last showing from backstack
 *
 * @param animation
 * Instance of current animation
 */
inline fun <reified T : IMediator> IFacade.showMeditator(mapName: String? = null, popLast: Boolean = false, animation: IAnimator? = null) {
    if (hasMediator<T>(mapName)) view.showMediator(mapName
            ?: T::class.className(), popLast, animation)
    else retrieveMediator<T>(mapName).show(animation, popLast)
}

/**
 * Register an `IProxy` with the `Model` by name.
 *
 * @param dataToHold
 * Contructor parameters that proxyLazy must apply
 *
 * @return IProxy instance with given parameters
 */
inline fun <reified T : IProxy<*>> IFacade.registerProxy(vararg dataToHold: Any): T = this.model.registerProxy(dataToHold.asList())

/**
 * Register an `ICommand` with the `Controller`.
 *
 * @param noteName
 * the name of the `INotification` to associate the
 * `ICommand` with.
 */
inline fun <reified T : ICommand> IFacade.registerCommand(noteName: String) {
    this.controller.registerCommand<T>(noteName)
}

/**
 * Retrieve a `IProxy` from the `Model` by name.
 *
 * @return the `IProxy` previously regisetered with the `Model`.
 */
inline fun <reified T : IProxy<*>> IFacade.retrieveProxy(params: List<Any>? = null): T = this.model.retrieveProxy(params)

/**
 * Remove an `IProxy` core from the `Model`
 */
inline fun <reified T : IProxy<*>> IFacade.removeProxy(): T? = this.model.removeProxy() as? T

/**
 * Check if a Proxy is registered.
 *
 * @return whether a Proxy is currently registered
 */
inline fun <reified T : IProxy<*>> IFacade.hasProxy(): Boolean = this.model.hasProxy<T>()


////------------- EXTENSIONS FUNCTION -----////

/**
 * Hide current mediator by the name and remove it from backstack then show last added mediator at backstack
 * If there is no mediator in backstack there is no action will be (only if bacstack size > 1)
 *
 * @param mediatorName
 * the name of the `IMediator` core to be removed from the screen
 */
fun IFacade.popMediator(mediatorName: String, animation: IAnimator? = null) {
    view.popMediator(mediatorName, animation)
}

/**
 * Hide current mediator by name
 *
 * @param mediatorName
 * the name of the `IMediator` core to be removed from the screen
 *
 * @param popIt
 * Indicates that is need to be removed from backstack
 *
 * @param animation
 * Instance of current animation
 */
fun IFacade.hideMediator(mediatorName: String, popIt: Boolean, animation: IAnimator?) {
    view.hideMediator(mediatorName, popIt, animation)
}

fun IFacade.registerObserver(notifName: String, notificator: INotificator) {
    view.registerObserver(notifName, Observer(notificator, this.multitonKey))
}

/**
 * Manually remove observer by given notification name and
 */
fun IFacade.removeObserver(notifName: String, observerContext:Any) {
    view.removeObserver(notifName, observerContext)
}

/**
 * Check if a Command is registered for a given Notification
 *
 * @param notificationName
 * @return whether a Command is currently registered for the given `notificationName`.
 */
fun IFacade.hasCommand(notificationName: String): Boolean = controller.hasCommand(notificationName)

/**
 * Remove a previously registered `ICommand` to `INotification` mapping from the Controller.
 *
 * @param notificationName the name of the `INotification` to remove the `ICommand` mapping for
 */
fun IFacade.removeCommand(notificationName: String) {
    this.controller.removeCommand(notificationName)
}

/**
 * Create and send an `INotification`.
 *
 * <P>
 * Keeps us from having to construct new notification
 * instances in our implementation code.
 * @param notificationName the name of the notification to send
 * @param body the body of the notification (optional)
 * @param type the type of the notification (optional)
</P>
 */
fun IFacade.sendNotification(notificationName: String, body: Any?, type: String?) {
    this.view.notifyObservers(Notification(notificationName, body, type))
}

/**
 * Remove this core by it's `multitonKey`
 */
fun IFacade.remove() {
    IFacade.removeCore(this.multitonKey)
}

/**
 * Manually handle hardware backbutton
 */
fun IFacade.handleBackButton(animation: IAnimator? = LinearAnimator()): Boolean {
    val backStack = view.mediatorBackStack
    // only if we has more than one mediator in backstack we manage backbutton by this
    if (backStack.size > 1) {
        return view.currentShowingMediator?.handleBackButton(animation) ?: false
    }
    return false
}