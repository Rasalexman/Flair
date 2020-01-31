package com.rasalexman.flaircore.view

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import com.rasalexman.flaircore.ext.clear
import com.rasalexman.flaircore.interfaces.*
import com.rasalexman.flaircore.patterns.observer.Notification
import java.lang.ref.WeakReference


/**
 * Created by a.minkin on 21.11.2017.
 */
class View : Fragment(), IView, Application.ActivityLifecycleCallbacks {

    /**
     * Bundle for store some data
     */
    override val stateBundle: Bundle
        get() = arguments ?: Bundle()
    /**
     * Main key for IFacade instance core
     */
    override var multitonKey: String = ""

    /**
     * Mapping of Notification names to Observer lists
     */
    override val observerMap by lazy {  hashMapOf<String, MutableList<IObserver>>() }
    /**
     *  Mapping of Mediator names to Mediator instances
     */
    override val mediatorMap by lazy {  hashMapOf<String, IMediator>() }

    /**
     * List of current added mediators on the screen
     */
    override val mediatorBackStack by lazy { mutableListOf<IMediator>() }
    /**
     * Current showing mediator
     */
    override var currentShowingMediator: IMediator? = null

    /**
     * Reference to the Activity attached on core
     */
    override var currentActivity: WeakReference<AppCompatActivity>? = null
    /**
     * Instance of ui container
     */
    override var currentContainer: ViewGroup? = null
    /**
     * is already registered lifecycle callbacks
     */
    private var isAlreadyRegistered: Boolean = false


    /**
     * Object creation CO
     */
    companion object ViewMapper : IMapper<View> {
        /**
         * State Bundle key
         */
        const val STATE_BUNDLE_KEY = "state_bundle_key"

        /**
         * ACTIVITY_CREATED lifecycle event
         */
        const val ACTIVITY_CREATED = "created"
        /**
         * ACTIVITY_STARTED lifecycle event
         */
        const val ACTIVITY_STARTED = "started"
        /**
         * ACTIVITY_RESUMED lifecycle event
         */
        const val ACTIVITY_RESUMED = "resumed"
        /**
         * ACTIVITY_PAUSED lifecycle event
         */
        const val ACTIVITY_PAUSED = "paused"
        /**
         * ACTIVITY_STOPPED lifecycle event
         */
        const val ACTIVITY_STOPPED = "stopped"
        /**
         * ACTIVITY_DESTROYED lifecycle event
         */
        const val ACTIVITY_DESTROYED = "destroyed"
        /**
         * ACTIVITY_STATE_SAVE lifecycle event
         */
        const val ACTIVITY_STATE_SAVE = "state_save"

        /**
         * Store for IView instances
         */
        override val instanceMap = hashMapOf<String, View>()

        /**
         * View Singleton Factory method.
         *
         * @return the Singleton core of `View`
         */
        @Synchronized
        fun getInstance(key: String): View = instance(key) {
            val viewInstance = View()
            viewInstance.multitonKey = key
            viewInstance
        }

        /**
         * Remove an IView core
         *
         * @param key of IView core to remove
         */
        @Synchronized
        fun removeView(key: String) {
            instanceMap.remove(key)?.clearAll()
        }
    }

    init {
        retainInstance = true
    }


    /**
     * Clear all mediators from map
     */
    private fun clearAll() {
        detachActivity()
        mediatorMap.forEach { (_, iMediator) ->
            iMediator.hide(null, true)
        }
        mediatorMap.clear()
        mediatorBackStack.clear()
    }

    /**
     * Attach current activity to the core view
     * Only one activity can be attached to the core
     *
     * @param activity
     * Current activity to be attached with lifecicle
     *
     * @param container
     * The container when ui will be added if there no container we take default activity decorView content (frame layout)
     */
    override fun attachActivity(activity: AppCompatActivity, container: ViewGroup?) {
        //this is because we need container to add views anyway
        currentContainer = container ?: activity.window.decorView.findViewById(android.R.id.content)
        // only if there is no attach
        if (!isAlreadyRegistered) {
            currentActivity = WeakReference(activity)
            activity.supportFragmentManager.beginTransaction().add(this, multitonKey).commitAllowingStateLoss()
            activity.application.registerActivityLifecycleCallbacks(this)
            isAlreadyRegistered = true
        }
    }

    /**
     * Detach current activity from view core
     */
    private fun detachActivity() {
        currentActivity?.get()?.let {
            val fragmentManager: FragmentManager? = (it as? AppCompatActivity)?.supportFragmentManager
            fragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
            // unregister life-circle callbacks
            it.application?.unregisterActivityLifecycleCallbacks(this)

            // clear mediator view and follow it's life-circle cause we need to recreate view, but don't need to remove from backstack
            mediatorBackStack.forEach { iMediator ->
                iMediator.hide()
                // we also need to clear mediator view cause it referenced to current activity that should be destroyed
                clearMediatorView(iMediator)
            }
            // clear container and it's reference
            currentContainer?.clear()
            currentContainer?.removeAllViews()
            currentContainer = null
            // clear the reference of current mediator
            currentShowingMediator = null
            // clear reference to the activity
            currentActivity?.clear()
            currentActivity = null
            //
            isAlreadyRegistered = false
        }
    }

    /**
     * Called when the IView is going to create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            stateBundle.putAll(savedInstanceState.getBundle(STATE_BUNDLE_KEY))
        }

        super.onCreate(savedInstanceState)
        // every view has options menu by default
        setHasOptionsMenu(true)
    }

    /**
     * Adapter method for creating menu
     * When menu is create we call the `currentShowingMediator` method
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        currentShowingMediator?.apply {
            if (hasOptionalMenu && !hideOptionalMenu) {
                onCreateOptionsMenu(menu, inflater)
            }
        }
    }

    /**
     * Adapter method for preparing menu
     */
    override fun onPrepareOptionsMenu(menu: Menu) {
        currentShowingMediator?.apply {
            if (hasOptionalMenu && !hideOptionalMenu) {
                onPrepareOptionsMenu(menu)
            }
        }
    }

    /**
     * Adapter method
     * When options item selected from menu
     *
     * @param item - menu item
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (currentShowingMediator?.onOptionsItemSelected(item) == true) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //-------------- LIFE CYCLE CALLBACKS -------/////
    /**
     * onActivityCreated lifecycle method
     *
     * @param activity - current activity instance
     * @param p1 - bundle for current activity
     */
    override fun onActivityCreated(activity: Activity?, p1: Bundle?) {
        notifyObservers(Notification(ACTIVITY_CREATED, p1))
    }

    /**
     * onActivityCreated lifecycle method
     */
    override fun onActivityStarted(activity: Activity?) {
        notifyObservers(Notification(ACTIVITY_STARTED, activity))
    }

    /**
     * onActivityResumed lifecycle method
     *
     * @param activity - current activity instance
     */
    override fun onActivityResumed(activity: Activity?) {
        notifyObservers(Notification(ACTIVITY_RESUMED, activity))
    }

    /**
     * onActivityPaused lifecycle method
     *
     * @param activity - current activity instance
     */
    override fun onActivityPaused(activity: Activity?) {
        notifyObservers(Notification(ACTIVITY_PAUSED, activity))
    }

    /**
     * onActivityStopped lifecycle method
     *
     * @param activity - current activity instance
     */
    override fun onActivityStopped(activity: Activity) {
        notifyObservers(Notification(ACTIVITY_STOPPED, activity))
        // if activity is finish there work we must clear the view
        // and all references to recreate view state manually when activity is wake up
        if (this.currentActivity?.get()?.isFinishing == true) detachActivity()
    }

    /**
     * onActivityDestroyed lifecycle method
     *
     * @param activity - current activity instance
     */
    override fun onActivityDestroyed(activity: Activity?) {
        notifyObservers(Notification(ACTIVITY_DESTROYED, activity))
        if (this.currentActivity?.get() == activity) detachActivity()
    }

    /**
     * onActivitySaveInstanceState lifecycle method
     *
     * @param activity - current activity instance
     * @param bundle - storage for values when activity no longer exist
     */
    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
        bundle.putBundle(STATE_BUNDLE_KEY, stateBundle)
        notifyObservers(Notification(ACTIVITY_STATE_SAVE, arguments))
        // only when activity change there configuration state ex rotate
        if (this.currentActivity?.get()?.isChangingConfigurations == true) detachActivity()
    }

    /**
     * onActivityResult returning method
     *
     * @param requestCode The Activity's onActivityResult requestCode
     * @param resultCode  The Activity's onActivityResult resultCode
     * @param data        The Activity's onActivityResult data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        currentShowingMediator?.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * When permission result responded
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        currentShowingMediator?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Check if permission already granted
     *
     * @param permissionToCheck
     * Name of the permission ex Manifest.permission.READ_CONTACTS
     */
    override fun checkSelfPermission(permissionToCheck: String): Int {
        return currentActivity?.get()?.let {
            ContextCompat.checkSelfPermission(it, permissionToCheck)
        } ?: -1
    }


    /////////------------------------------------///////

    /**
     * Private access to clear view on IMediator instance and follow mediator lifecycle
     *
     * @param mediator - mediator for clearing
     */
    override fun clearMediatorView(mediator: IMediator?) {
        mediator?.apply {
            (viewComponent as? ViewGroup)?.clear()
            viewComponent = null
            isDestroyed = true
            onDestroyView()
        }
    }

    /**
     * Main init function
     */
    init {
        arguments = Bundle()
    }
}