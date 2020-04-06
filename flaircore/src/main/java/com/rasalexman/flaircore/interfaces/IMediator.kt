package com.rasalexman.flaircore.interfaces

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View

/**
 * Created by a.minkin on 21.11.2017.
 */
interface IMediator : INotifier {
    /**
     * Current view for IMediator instance
     */
    var viewComponent: View?
    /**
     * Does options menu activated
     */
    var hasOptionalMenu: Boolean
    /**
     * If we need to hide options menu
     */
    var hideOptionalMenu: Boolean
    /**
     * Does IMedaitor `viewComponent` added to root container
     */
    var isAdded: Boolean
    /**
     * Does IMediator `viewComponent` was destroyed
     */
    var isDestroyed: Boolean

    /**
     * Does this mediator need to be added in backstack.
     * It's true by default
     * so framework is automatically navigate you by his backstack store
     */
    var isAddToBackStack:Boolean

    /**
     * Current IMediator name for retrieving
     */
    var mediatorName: String

    /**
     * Bundle arguments
     */
    val arguments: Bundle
    /**
     * List of notification names that this IMediator is listening for
     */
    val listNotificationInterests: MutableList<String>

    /**
     * When current View has attached your menu
     */
    fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)

    /**
     * Fragment legacy menu creation
     */
    fun onPrepareOptionsMenu(menu: Menu)

    /**
     * Menu Item Selection section
     */
    fun onOptionsItemSelected(item: MenuItem): Boolean

    /**
     * Respond function from current activity
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    /**
     * WHEN REQUESTED PERMISSIONS IS GRANTED
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)

    /**
     * Main initialize ViewComponent Function
     *
     * @param context - context for create layout with
     */
    fun createLayout(context: Context): View

    /**
     * It's called before view created
     * If we need to prepare some things before view is created ex get saved params from bundle
     */
    fun onPrepareCreateView()

    /**
     * Called by the create mediator view once
     *
     * @param view - created view
     */
    fun onCreatedView(view: View)

    /**
     * Called when mediator added to view container
     *
     * @param view - the view instance that added to container
     */
    fun onAddedView(view: View)

    /**
     * Called when mediator view removed from parent
     *
     * @param view - removed view instance
     */
    fun onRemovedView(view: View)

    /**
     * When view is destroyed or equal to null
     */
    fun onDestroyView()


    /**
     * Called by the View when the Mediator is registered.
     */
    fun onRegister()

    /**
     * Called by the View when the Mediator is removed.
     */
    fun onRemove()

    /**
     * Handle the hardware back button
     *
     * @param animation
     * Current animation changer
     */
    fun handleBackButton(animation: IAnimator? = null): Boolean

    /**
     * Called when animation is starting
     *
     * @param isShow
     * Flag that indicates is this starting a showing animation for current mediator
     */
    fun onAnimationStart(isShow: Boolean)

    /**
     * Called when animation is finished
     *
     * @param isShow
     * Flag that indicates is this starting a showing animation for current mediator
     */
    fun onAnimationFinish(isShow: Boolean)
}