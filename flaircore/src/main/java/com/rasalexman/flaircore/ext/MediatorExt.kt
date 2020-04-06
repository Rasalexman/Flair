package com.rasalexman.flaircore.ext

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.rasalexman.flaircore.interfaces.IAnimator
import com.rasalexman.flaircore.interfaces.IMediator


/**
 * Inflate current view component
 */
fun IMediator.inflateView(layoutId: Int): View = LayoutInflater.from(activity).inflate(layoutId, null)

/**
 * Start Activity with given intant and request code
 */
fun IMediator.startActivityForResult(intent: Intent, requestCode: Int, options: Bundle? = null) {
    facade.view.startActivityForResult(intent, requestCode, options)
}

/**
 * Start another activity with bundle options or not
 */
fun IMediator.startActivity(intent: Intent, bundle: Bundle? = null) {
    bundle?.let {
        activity.startActivity(intent, it)
    } ?: activity.startActivity(intent)
}

/**
 * Request Permissions from user
 */
fun IMediator.requestPermissions(permissions: Array<String>, requestCode: Int) {
    facade.view.requestPermissions(permissions, requestCode)
}

/**
 * Check the given permission to be approved by user or system
 */
fun IMediator.checkSelfPermission(permissionToCheck: String): Int = facade.view.checkSelfPermission(permissionToCheck)

/**
 * Should we show permission description dialog for user
 */
fun IMediator.shouldShowRequestPermissionRationale(permission: String): Boolean = facade.view.shouldShowRequestPermissionRationale(permission)

/**
 * Register a list of `INotification` interests.
 *
 * @param listNotification
 * array of string notification names
 *
 * @param notificator
 * the callback function
 *
 */
fun IMediator.registerListObservers(listNotification: List<String>, notificator: INotificator): IMediator {
    listNotification.forEach {
        registerObserver(it, notificator)
    }
    return this
}

/**
 * Register observer function with notifName
 *
 * @param notifName
 * Name of notification that recieve notificator
 *
 * @param notificator
 * This is simple closure function callback
 *
 */
fun IMediator.registerObserver(notifName: String, notificator: INotificator): IMediator {
    listNotificationInterests.add(notifName)
    facade.registerObserver(notifName, notificator)
    return this
}

/**
 * Remove observer by given notification name
 *
 * @param notifName
 * Name of notification that recieve notificator
 */
fun IMediator.removeObserver(notifName: String) {
    listNotificationInterests.remove(notifName)
    facade.removeObserver(notifName, this.multitonKey)
}

/**
 * Remove all notifications from current IMediator implementation instance
 */
fun IMediator.removeAllObservers() {
    listNotificationInterests.forEach {
        facade.removeObserver(it, this.multitonKey)
    }
    listNotificationInterests.clear()
}

/**
 * Add current mediator to view stage
 *
 * @param popLast
 * Flag that indicates to need remove last showing mediator from backstack
 */
fun IMediator.show(animation: IAnimator? = null, popLast: Boolean = false) {
    facade.showMediator<IMediator>(this.mediatorName, popLast, animation)
}

/**
 * Remove current mediator from view stage
 *
 * @param animation
 * Current hide animation
 *
 * @param popIt
 * Flag that indicates to need remove current mediator from backstack
 */
fun IMediator.hide(animation: IAnimator? = null, popIt: Boolean = false) {
    facade.hideMediator(this.mediatorName, popIt, animation)
}


/**
 * Hide current mediator and remove it from backstack
 * Then show last added mediator from backstack
 *
 * @param animation
 * Current hiding animation for pop
 */
fun IMediator.popToBack(animation: IAnimator? = null) {
    facade.popMediator(this.mediatorName, animation)
}

/**
 * Retrieve lazy mediator core by given generic class
 *
 * @param mediatorName
 * Mediator name to be retrieved by lazy function
 */
inline fun <reified T : IMediator> IMediator.mediatorLazy(mediatorName: String? = null): Lazy<T> = lazy {
    mediator<T>(mediatorName)
}

/**
 * Retrieve lazy mediator core by given generic class
 *
 * @param mediatorName
 * Mediator name to be retrieved
 */
inline fun <reified T : IMediator> IMediator.mediator(mediatorName: String? = null): T = facade.retrieveMediator(mediatorName)

/**
 * Remove an `IMediator` from the `View` core.
 */
inline fun <reified T : IMediator> IMediator.removeMediator(mediatorName: String? = null) {
    facade.removeMediator<T>(mediatorName)
}


/**
 * GO Back to given mediatorName(high priority) or Generic class name.
 *
 * @param mediatorName
 * Mediator to which you go back, so if there is no registered mediatorLazy by this name new instance is create as back stacked
 *
 * @param animation
 * Current hiding animation for pop
 */
inline fun <reified T : IMediator> IMediator.popTo(mediatorName: String? = null, animation: IAnimator? = null) {
    facade.retrieveMediator<T>(mediatorName).show(animation, true)
}

/**
 * Show the given generic mediatorLazy or by name
 *
 * @param mediatorName
 * Mediator name to be showed
 *
 * @param builder
 * Mediator builder lambda use to instantiate
 */
inline fun <reified T : IMediator> IMediator.showMediator(animation: IAnimator? = null, mediatorName: String? = null, noinline builder:(()->T)? = null) {
    return when {
        facade.hasMediator<T>(mediatorName) -> facade.retrieveMediator<T>(mediatorName).show(animation)
        builder != null -> facade.registerMediator(mediatorName, builder).show(animation)
        else -> throw RuntimeException("You need to register your mediator first with `builder` function or use reflection package instead")
    }
}