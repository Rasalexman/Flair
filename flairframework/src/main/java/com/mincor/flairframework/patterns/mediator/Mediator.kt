package com.mincor.flairframework.patterns.mediator

import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.mincor.flairframework.interfaces.IMediator
import com.mincor.flairframework.patterns.observer.Notifier


/**
 * Created by a.minkin on 21.11.2017.
 */
abstract class Mediator : Notifier(), IMediator {

    override var viewComponent: View? = null            // current view of mediator
    override var hasOptionalMenu:Boolean = false        // does view get opt menu
    override var hideOptionalMenu: Boolean = false      // Is hide optional menu
    override var mediatorName: String? = null           // Current mediator name for put in backStack

    /**
     * List the `INotification` names this `Mediator`
     * is interested in being notified of.
     *
     * @return String[] the list of `INotification` names
     */
    override val listNotificationInterests: ArrayList<String> = arrayListOf()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {}
    override fun onPrepareOptionsMenu(menu: Menu) {}
    override fun onOptionsItemSelected(item: MenuItem): Boolean = true

    ///---------- VIEW LIFECIRCLE --------///
    /**
     * When view was created but not added to the stage
     */
    override fun onCreatedView(view: View) {}

    /**
     * Called by the View when the Mediator is registered.
     */
    override fun onAddedView(view: View) {}

    /**
     * Called when `viewComponent` removed from parent
     */
    override fun onRemovedView(view: View) {}

    /**
     * Called by the View when viewComponent is null
     */
    override fun onDestroyView() {}
    /////------------------------/////

    ///--------- MEDIATOR LIFECIRCLE --------///
    /**
     * Called by the View when the Mediator is registered.
     */
    override fun onRegister() {}

    /**
     * Called by the View when the Mediator is removed.
     */
    override fun onRemove() {}
    /////------------------------/////

    /**
     * Called when meditor request onStartActivityForResult method
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { }

    /**
     * Request permission to access your app additional properties of os ex. camera, contacts, locations
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {}
}

