package com.mincor.flairframework.interfaces

import android.content.Context
import android.view.ViewGroup
import com.mincor.flairframework.core.FlairActivity
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
     * Activity for attachment and life-circle handled
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
            if(context == null) throw RuntimeException("You need to specified `context` for this core")
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
 * GO Back to given mediatorName(high priority) or Generic class name.
 *
 * @param mediatorName
 * Mediator to which you go back, so if there is no registered mediator by this name new instance is create as back stacked
 */
inline fun <reified T : IMediator> IMediator.popTo(mediatorName: String? = null, animation: IAnimator? = null) {
    facade.retrieveMediator<T>(mediatorName ?: T::class.className()).show(animation, true)
}

/**
 * Register an `IMediator` core with the `View`.
 */
inline fun <reified T : IMediator> IFacade.registerMediator(mediatorName: String? = null) {
    this.view.registerMediator<T>(mediatorName)
}

/**
 * Remove a `IMediator` core from the `View`.
 */
inline fun <reified T : IMediator> IFacade.removeMediator(): T? = this.view.removeMediator<T>() as? T

/**
 * Register an `IProxy` with the `Model` by name.
 *
 * @param dataToHold
 * Contructor parameters as example: `mapOf("view" to Mediator@this)`
 *
 * @return IProxy instance with given parameters
 */
inline fun <reified T : IProxy<*>> IFacade.registerProxy(dataToHold: Map<String, Any>? = null):T = this.model.registerProxy(dataToHold)

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
inline fun <reified T : IProxy<*>> IFacade.retrieveProxy(): T = this.model.retrieveProxy()

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

/**
 * Show last added IMediator from backstack. If there is no mediator in backstack show the one passed before
 */
inline fun <reified T : IMediator> IFacade.showLastOrExistMediator(animation: IAnimator? = null) {
    view.showLastOrExistMediator<T>(animation)
}

/**
 * Check if a IMediator is registered or not
 */
inline fun <reified T : IMediator> IFacade.hasMediator(): Boolean = view.hasMediator<T>()


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

/**
 * Show current selected mediator
 *
 * @param mapName
 * This si a current mediator name to put in backstack
 *
 * @param popLast
 * flag that indicates need to remove last showing from backstack
 *
 * @param animation
 * Instance of current animation
 */
fun IFacade.showMeditator(mapName: String, popLast: Boolean, animation: IAnimator?) {
    view.showMediator(mapName, popLast, animation)
}

fun IFacade.registerObserver(notifName: String, notificator: INotificator) {
    view.registerObserver(notifName, Observer(notificator, this.multitonKey))
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