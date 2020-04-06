package com.rasalexman.flaircore.patterns.mediator

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.rasalexman.flaircore.ext.popToBack
import com.rasalexman.flaircore.interfaces.IAnimator
import com.rasalexman.flaircore.interfaces.IMediator
import com.rasalexman.flaircore.patterns.observer.Notifier


/**
 * Created by a.minkin on 21.11.2017.
 */
abstract class Mediator : Notifier, IMediator {

    /**
     * Main construction function
     */
    constructor()

    /**
     * Secondary construction function with [multitonKey]
     */
    constructor(multitonKey:String) : super(multitonKey)

    /**
     * current view of mediator
     */
    override var viewComponent: View? = null

    /**
     * Does view get opt menu
     */
    override var hasOptionalMenu: Boolean = false

    /**
     * Is hide optional menu
     */
    override var hideOptionalMenu: Boolean = false

    /**
     * Does mediator view added to hosted view container
     */
    override var isAdded: Boolean = false

    /**
     * Does current view destroyed
     */
    override var isDestroyed: Boolean = false

    /**
     * Does it's need to be added in backstack
     */
    override var isAddToBackStack: Boolean = true

    /**
     * Current mediator name for put in backStack
     */
    override var mediatorName: String = ""
        get() {
            return field.takeIf { it.isNotEmpty() } ?: this::class.toString()
        }

    /**
     * Bundle arguments
     */
    override val arguments: Bundle
        get() = facade.view.stateBundle

    /**
     * List the `INotification` names this `Mediator`
     * is interested in being notified of.
     *
     * @return String[] the list of `INotification` names
     */
    override val listNotificationInterests by lazy { mutableListOf<String>() }

    /**
     * When current View has attached your menu
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = Unit
    /**
     * Fragment legacy menu creation
     */
    override fun onPrepareOptionsMenu(menu: Menu) = Unit
    /**
     * Menu Item Selection section
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean = true

    ///---------- VIEW LIFECYRCLE --------///
    /**
     * Called before create view instance
     */
    override fun onPrepareCreateView() = Unit

    /**
     * When view was created but not added to the stage
     */
    override fun onCreatedView(view: View) = Unit

    /**
     * Called by the View when the Mediator is registered.
     */
    override fun onAddedView(view: View) = Unit

    /**
     * Called when `viewComponent` removed from parent
     */
    override fun onRemovedView(view: View) = Unit

    /**
     * Called by the View when viewComponent is null
     */
    override fun onDestroyView() = Unit
    /////------------------------/////

    ///--------- MEDIATOR LIFE-CIRCLE --------///
    /**
     * Called by the View when the Mediator is registered.
     */
    override fun onRegister() = Unit

    /**
     * Called by the View when the Mediator is removed.
     */
    override fun onRemove() = Unit
    /////------------------------/////

    ////---------- Animation change handlers -------////
    /**
     * Called when animation is starting
     *
     * @param isShow
     * Flag that indicates is this starting a showing animation for current mediator
     */
    override fun onAnimationStart(isShow: Boolean) = Unit

    /**
     * Called when animation is finished
     *
     * @param isShow
     * Flag that indicates is this starting a showing animation for current mediator
     */
    override fun onAnimationFinish(isShow: Boolean) = Unit
    ////--------------------------////

    /**
     * Called when meditor request onStartActivityForResult method
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) = Unit

    /**
     * Request permission to access your app additional properties of os ex. camera, contacts, locations
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) = Unit


    /**
     * Hardware button back handler
     *
     * @param animation - back animation
     */
    override fun handleBackButton(animation: IAnimator?): Boolean {
        return if (isAdded && !isDestroyed) {
            popToBack(animation)
            true
        } else false
    }
}

