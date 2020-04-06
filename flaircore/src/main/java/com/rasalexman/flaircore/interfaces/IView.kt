package com.rasalexman.flaircore.interfaces

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import java.lang.ref.WeakReference

/**
 * Created by a.minkin on 21.11.2017.
 */
interface IView : IMultitonKey {

    /**
     * Bundle for save parameters when configuration change
     */
    val stateBundle: Bundle

    /**
     * Storage for all IMedaitor instances when it gonna be registered
     */
    val mediatorMap: HashMap<String, IMediator>
    /**
     * Storage for notifications name that we gonna notify
     */
    val observerMap: HashMap<String, MutableList<IObserver>>
    /**
     * Notification map for save current notification
     */
    val notificationMap: HashMap<String, INotification>
    /**
     * IMediator `viewComponent` backstack. This is a main place for storing navigation
     */
    val mediatorBackStack: MutableList<IMediator>
    /**
     * This is a reference to the mediator that on the screen now
     */
    var currentShowingMediator: IMediator?

    /**
     * Reference to the Activity attached on core
     */
    var currentActivity: WeakReference<FragmentActivity>?

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
    fun attachActivity(activity: FragmentActivity, container: ViewGroup? = null)

    /**
     * When requested activity has come
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    /**
     * Start another activity and read result from it
     *
     * @param intent
     * @param requestCode
     * @param options
     */
    fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?)

    /**
     * Request permission to access your app additional properties of os ex. camera, contacts, locations
     *
     * @param permissions - permissions list to request
     * @param requestCode - returning code
     */
    fun requestPermissions(permissions: Array<String>, requestCode: Int)

    /**
     * Check self permission for current mediator
     *
     * @param permissionToCheck - single permission name to check for
     */
    fun checkSelfPermission(permissionToCheck: String): Int

    /**
     * Check should we show message about permissions
     *
     * @param permissionName
     */
    fun shouldShowRequestPermissionRationale(permissionName: String): Boolean

    /**
     * Clear mediator view but do't remove it from backstack for recreating again
     *
     * @param mediator - mediator to clean
     */
    fun clearMediatorView(mediator: IMediator?)

    /**
     * Get Bundle arguments it's always exist cause we create it when IView instance is initialized
     */
    fun getArguments(): Bundle?
}