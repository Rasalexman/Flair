package com.rasalexman.flaircore.ext

import com.rasalexman.flaircore.interfaces.*


////////--------- INLINE SECTION ------////
/**
 * Check if a Mediator is registered or not
 *
 * @param mediatorName
 * Given mediator name
 */
inline fun <reified T : IMediator> IView.hasMediator(mediatorName: String? = null): Boolean = mediatorMap[mediatorName ?: T::class.toString()] != null

/**
 * Remove an `IMediator` from the `View`.
 */
inline fun <reified T : IMediator> IView.removeMediator(mediatorName: String? = null): IMediator? {
    val currentName = mediatorName ?: T::class.toString()
    // Retrieve the named mediator
    val mediator = mediatorMap[currentName]
    mediator?.apply {
        // hide mediator and remove from backstack
        hideMediator(currentName, true, null)
        removeAllObservers()

        // clear all references
        this.multitonKey = ""
        this.mediatorName = ""
        // alert the mediator that it has been removed
        onRemove()
    }
    return mediatorMap.remove(currentName)
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
    val clazzName = mediatorName ?: T::class.toString()
    return if (mediatorMap.containsKey(clazzName)) {
        this.mediatorMap[clazzName] as T
    } else {
        throw RuntimeException("There is no mediator with given mediator name $clazzName")
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
inline fun <reified T : IMediator> IView.registerMediator(mediatorName: String? = null, mediatorBuilder: () -> T): T {
    val clazz = T::class
    val clazzName = mediatorName ?: clazz.toString()
    // Register the Mediator for retrieval by name
    val currentMediator = this.mediatorMap.getOrPut(clazzName) { mediatorBuilder() }
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
 * Indicates that IMediator is need to be removed from backstack and clear viewComponent
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
        } ?: mediator.apply {
            if (isAdded) {
                // if we have view component to remove from parent
                viewComponent?.let {
                    it.removeFromParent()
                    // notify lifecycle of mediator
                    onRemovedView(it)
                }
                isAdded = false
            }
            // if flag `true` we remove mediator from backstack and clear view
            if (popIt) {
                if (mediatorBackStack.contains(this)) mediatorBackStack.remove(this)
                // only if we has our mediator into backstack
                clearMediatorView(this)
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
    // if showing mediator the same we just return
    if (currentShowingMediator == lastMediator) {
        return
    }
    //
    currentShowingMediator?.apply {
        viewComponent = viewComponent ?: let {
            onPrepareCreateView()
            val layout = createLayout(activity)
            onCreatedView(layout)
            layout
        }
        // mark mediator as not destroyed cause we already create a `viewComponent`
        isDestroyed = false

        // indicator to animation direction
        var isShowAnimation = true
        // add to backstack if we don't have any mediators in it or last mediator does not equal the same mediator as we showing on the screen
        if (isAddToBackStack) {
            isShowAnimation = animation?.isShow ?: true

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
                    // we don't need to remove last mediator `viewComponent`
                    if (mediator.mediatorName == lastMediator?.mediatorName) {
                        // only if we have an animation we simulate back and clear the `viewComponent` after animation
                        mediatorBackStack.remove(mediator)
                        isShowAnimation = false
                        continue
                    }
                    mediator.hide(null, true)
                }
            }
        }


        // check for optional menu and invalidate it if it has
        if (hasOptionalMenu && !hideOptionalMenu) {
            currentActivity?.get()?.invalidateOptionsMenu()
        }

        // if we have animation we play it
        animation?.apply {
            from = lastMediator
            to = currentShowingMediator
            isShow = isShowAnimation
            popLast = popLastMediator
            playAnimation()

        } ?: lastMediator?.hide(null, popLastMediator)

        // safe add view to container
        viewComponent?.apply {
            currentContainer?.let {
                this.removeFromParent()
                it.addView(this)
                this.x = 0f
                this.y = 0f
                // mark flag about `viewComponent` is already added to hosted view container
                isAdded = true
                // call the lifecycle of view
                onAddedView(this)
            }
        }
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
    this.observerMap[notificationName]?.let { list ->
        // Remove predicate notifications, also we clear observable references
        list.removeAll { observer ->
            if (observer.compareNotifyContent(notifyContext)) observer.clear()
            else false
        }
        // Also, when a Notification's Observer list length falls to
        // zero, delete the notification key from the observer map
        if (list.isEmpty()) {
            observerMap.remove(notificationName)
        }
        notificationMap.remove(notificationName)
    }
}

/**
 * Register an `Observer` to be notified of
 * `INotifications` with a given name and automatically notify registered observers
 *
 * @param notificationName
 * the name of the `Notifications` to notify this
 * `Observer` of
 * @param observer
 * the `Observer` to register
 */
fun IView.registerObserver(notificationName: String, observer: IObserver) {
    val observers = this.observerMap.getOrPut(notificationName) { mutableListOf() }
    observers.add(observer)
    notificationMap.remove(notificationName)?.let(observer::notifyObserver)
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
 * @param notification
 * the `Notification` to notify
 * `Observers` of.
 */
fun IView.notifyObservers(notification: INotification) {
    val notificationName = notification.name
    this.observerMap[notificationName]?.let {
        // Copy observers from reference array to working array,
        // since the reference array may change during the
        // notification loop
        val workingObservers = it.toTypedArray()
        // Notify Observers from the working array
        workingObservers.forEach { iObserver ->
            iObserver.notifyObserver(notification)
        }
    } ?: let {
        notificationMap[notificationName] = notification
    }
}