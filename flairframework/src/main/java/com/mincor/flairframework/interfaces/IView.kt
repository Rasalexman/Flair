package com.mincor.flairframework.interfaces

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.ViewGroup
import com.mincor.flairframework.core.FlairActivity
import com.mincor.flairframework.ext.className
import com.mincor.flairframework.ext.clear
import com.mincor.flairframework.ext.createInstance
import com.mincor.flairframework.ext.removeFromParent

/**
 * Created by a.minkin on 21.11.2017.
 */
interface IView : IMultitonKey {

    val mediatorMap: MutableMap<String, IMediator>
    val observerMap: MutableMap<String, MutableList<IObserver>>
    val mediatorBackStack: MutableList<IMediator>
    var currentShowingMediator: IMediator?

    /**
     * Reference to the Activity attached on core
     */
    var currentActivity: FlairActivity?

    /**
     * Instance of ui container
     */
    var currentContainer: ViewGroup?


    /**
     * Attach current activity to the core view
     * Only one activity can be attached to the core
     *
     * @param activity
     * Current activity to be attached with lifecycle
     *
     * @param container
     * The container when ui will be added
     */
    fun attachActivity(activity: FlairActivity, container: ViewGroup? = null)

    /**
     * When requested activity has come
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    /**
     * Start another activity and read result from it
     */
    fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?)

    /**
     * Request permission to access your app additional properties of os ex. camera, contacts, locations
     */
    fun requestPermissions(permissions: Array<String>, requestCode: Int)

    /**
     *
     */
    fun checkSelfPermission(permissionToCheck:String):Int

    /**
     *
     */
    fun shouldShowRequestPermissionRationale(permission: String): Boolean
}


////////--------- INLINE SECTION ------////
/**
 * Check if a Mediator is registered or not
 *
 * @param mediatorName
 * Given mediator name
 */
inline fun <reified T : IMediator> IView.hasMediator(mediatorName:String? = null): Boolean = mediatorMap[mediatorName?:T::class.className()] != null

/**
 * Remove an `IMediator` from the `View`.
 */
inline fun <reified T : IMediator> IView.removeMediator(mediatorName:String? = null): IMediator? {
    val currentName = mediatorName ?: T::class.className()
    // Retrieve the named mediator
    val mediator = mediatorMap[currentName]
    mediator?.let {
        // hide mediator and remove from backstack
        it.hide(null, true)
        // for every notification this mediator is interested in...
        val interests = it.listNotificationInterests
        // remove the observer linking the mediator
        // to the notification interest
        interests.forEach { s ->
            removeObserver(s, it.multitonKey)
        }
        interests.clear()

        // remove the mediator from the map
        mediatorMap.remove(mediatorName)
        // alert the mediator that it has been removed
        it.onRemove()
    }
    return mediator
}

/**
 * Show last added IMediator from backstack. If there is no mediator in backstack show the one passed by name
 *
 * @param animation
 * the name of the `IMediator` animation to show on the screen
 */
inline fun <reified T : IMediator> IView.showLastOrExistMediator(animation: IAnimator? = null) {
    val lastMediator = mediatorBackStack.lastOrNull()
    lastMediator?.show(animation, true) ?: retrieveMediator<T>().show(animation)
}

/**
 * Retrieve an `IMediator` from the `View`.
 */
inline fun <reified T : IMediator> IView.retrieveMediator(mediatorName: String? = null): T {
    val clazzName = mediatorName ?: T::class.className()
    return if (mediatorMap.containsKey(clazzName)) {
        this.mediatorMap[clazzName] as T
    } else {
        registerMediator(clazzName)
    }
}


/**
 * Register an `IMediator` core with the `View`.
 *
 * <P>
 * Registers the `IMediator` so that it can be retrieved by
 * name, and further interrogates the `IMediator` for its
 * `INotification` interests.
</P> *
 */
inline fun <reified T : IMediator> IView.registerMediator(mediatorName: String? = null): T {
    val clazz = T::class
    val clazzName = mediatorName ?: clazz.className()
    // Register the Mediator for retrieval by name
    val currentMediator = this.mediatorMap.getOrPut(clazzName) { clazz.createInstance() }
    // Only fresh mediators can register observers
    if (currentMediator.multitonKey.isEmpty()) {
        currentMediator.multitonKey = this.multitonKey
        currentMediator.mediatorName = clazzName
    }
    // call mediator lifecycle function
    currentMediator.onRegister()
    return currentMediator as T
}

/////----------- EXTENSIONS FUNCTION
/**
 * Hide current mediator by the name and remove it from backstack then show last added mediator at backstack
 * If there is no mediator in backstack there is no action will be (only if backstack size > 1)
 *
 * @param mediatorName
 * the name of the `IMediator` core to be removed from the screen
 */
fun IView.popMediator(mediatorName: String, animation: IAnimator? = null) {
    mediatorMap[mediatorName]?.let { mediatorToPop ->
        // if mediator to pop equal current showing mediator and backstack has more than one mediator
        if (mediatorToPop == currentShowingMediator && mediatorBackStack.size > 1) {

            // get last added mediator from backstack and show it on the screen
            val lastIndex = mediatorBackStack.lastIndexOf(mediatorToPop) - 1
            val lastAddedMediator = mediatorBackStack[lastIndex]
            lastAddedMediator.show(animation, true)
        }
    }
}

/**
 * Hide current mediator by name
 *
 * @param mediatorName
 * the name of the `IMediator` core to be removed from the screen
 *
 * @param popIt
 * Indicates that IMediator is need to be removed from backstack and viewComponent cleared
 *
 * @param animation
 * Simple hide animation
 */
fun IView.hideMediator(mediatorName: String, popIt: Boolean, animation: IAnimator?) {
    mediatorMap[mediatorName]?.let { mediator ->
        animation?.let { anim ->
            // play animation
            anim.apply {
                from = mediator
                isShow = false
                popLast = popIt
                playAnimation()
            }
        } ?: let {
            // if we have view component to remove from parent
            mediator.viewComponent?.let {
                it.removeFromParent() // remove viewComponent from ui layer
                mediator.onRemovedView()
            }
            // if flag `true` we remove mediator from backstack and clear view
            if (popIt) {
                (mediator.viewComponent as? ViewGroup)?.clear()
                mediator.viewComponent = null
                if (mediatorBackStack.contains(mediator))
                    mediatorBackStack.remove(mediator)
                mediator.onDestroyView()

                //val names: String = mediatorBackStack.joinToString { it.mediatorName ?: "" }
                //Log.w("------->", "BACKSTACK AFTER POP  $names")
            }
        }
    }
}

/**
 * Show current selected mediator
 *
 * @param mediatorName
 * Current name of mediator
 *
 * @param popLastMediator
 * flag that indicates need to remove last showing from backstack and clear view
 */
fun IView.showMediator(mediatorName: String, popLastMediator: Boolean, animation: IAnimator? = null) {
    val lastMediator = currentShowingMediator
    // Retrieve the named mediator
    currentShowingMediator = mediatorMap[mediatorName]
    // if showing mediator the same just return
    if (currentShowingMediator == lastMediator) {
        return
    }
    currentShowingMediator?.apply {
        viewComponent = viewComponent ?: let {
            val layout = createLayout(activity)
            onCreatedView(layout)
            layout
        }

        // check for optional menu and invalidate it if it has
        if (hasOptionalMenu) {
            currentActivity?.invalidateOptionsMenu()
        }

        // indicator to animation direction
        var isShowAnimation = true
        // add to backstack if we don't have any mediators in it or last mediator does not equal the same mediator as we showing on the screen
        if (!mediatorBackStack.contains(this)) {
            if (!popLastMediator) {
                mediatorBackStack.add(this)
            } else {
                val indexOf = mediatorBackStack.indexOf(lastMediator)
                mediatorBackStack.add(indexOf, this)
            }
        } else {
            val indexOf = mediatorBackStack.indexOf(this) + 1
            while (mediatorBackStack.size > indexOf) {
                val mediator = mediatorBackStack[indexOf]
                // we dont need to remove last mediator `viewComponent`
                if (mediator.mediatorName == lastMediator?.mediatorName) {
                    // only if we have an animation we simulate back
                    mediatorBackStack.remove(mediator)
                    isShowAnimation = false
                    continue
                }
                mediator.hide(null, true)
            }
        }

        animation?.apply {
            from = lastMediator
            to = currentShowingMediator
            isShow = isShowAnimation
            popLast = popLastMediator
            playAnimation()
        } ?: lastMediator?.hide(null, popLastMediator)

        currentContainer?.addView(viewComponent)
        viewComponent?.x = 0f
        viewComponent?.y = 0f
        onAddedView()
    }
}

/**
 * Remove the observer for a given notifyContext from an observer list for a given Notification name.
 * <P>
 * @param notificationName which observer list to remove from
 * @param notifyContext remove the observer with this object as its notifyContext
</P> */
fun IView.removeObserver(notificationName: String, notifyContext: Any) {
    // the observer list for the notification under inspection
    this.observerMap[notificationName]?.let {
        // Remove predicate notifications, also we clear observable references
        it.removeAll {
            if (it.compareNotifyContext(notifyContext))
                it.clear()
            else false
        }
        // Also, when a Notification's Observer list length falls to
        // zero, delete the notification key from the observer map
        if (it.isEmpty()) {
            observerMap.remove(notificationName)
        }
    }
}

/**
 * Register an `Observer` to be notified of
 * `INotifications` with a given name.
 *
 * @param noteName
 * the name of the `Notifications` to notify this
 * `Observer` of
 * @param observer
 * the `Observer` to register
 */
fun IView.registerObserver(noteName: String, observer: IObserver) {
    val observers = this.observerMap.getOrPut(noteName) { mutableListOf() }
    observers.add(observer)
}

/**
 * Notify the `Observers` for a particular
 * `Notification`.
 *
 * <P>
 * All previously attached `Observers` for this
 * `Notification`'s list are notified and are passed a
 * reference to the `Notification` in the order in which they
 * were registered.
</P> *
 *
 * @param note
 * the `Notification` to notify
 * `Observers` of.
 */
fun IView.notifyObservers(note: INotification) {
    this.observerMap[note.name]?.let {
        // Copy observers from reference array to working array,
        // since the reference array may change during the
        // notification loop
        val workingObservers = it.toTypedArray()
        // Notify Observers from the working array
        workingObservers.forEach { iObserver ->
            iObserver.notifyObserver(note)
        }
    }
}