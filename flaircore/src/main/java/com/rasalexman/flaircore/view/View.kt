package com.rasalexman.flaircore.view

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
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

    override val stateBundle: Bundle
        get() = arguments ?: Bundle()
    /**
     * Main key for IFacade instance core
     */
    override var multitonKey: String = ""

    // Mapping of Notification names to Observer lists
    override val observerMap = HashMap<String, MutableList<IObserver>>()
    // Mapping of Mediator names to Mediator instances
    override val mediatorMap = HashMap<String, IMediator>()

    // List of current added mediators on the screen
    override val mediatorBackStack: MutableList<IMediator> = mutableListOf()
    // Current showing mediator
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


    companion object ViewMapper : IMapper<View> {
        const val STATE_BUNDLE_KEY = "state_bundle_key"

        const val ACTIVITY_CREATED = "created"
        const val ACTIVITY_STARTED = "started"
        const val ACTIVITY_RESUMED = "resumed"
        const val ACTIVITY_PAUSED = "paused"
        const val ACTIVITY_STOPPED = "stopped"
        const val ACTIVITY_DESTROYED = "destroyed"
        const val ACTIVITY_STATE_SAVE = "state_save"

        override val instanceMap = HashMap<String, View>()
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
            val fragmentManager = activity.supportFragmentManager
            fragmentManager?.beginTransaction()?.add(this, multitonKey)?.commitAllowingStateLoss()
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
            // unregister lifecyrcle callbacks
            it.application?.unregisterActivityLifecycleCallbacks(this)
            
            // clear mediator view and follow it's lifecyrcle cause we need to recreate view, but don't need to remove from backstack
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
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (currentShowingMediator?.onOptionsItemSelected(item) == true) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //-------------- LIFE CYCLE CALLBACKS -------/////
    override fun onActivityCreated(activity: Activity?, p1: Bundle?) {
        notifyObservers(Notification(ACTIVITY_CREATED, p1))
    }

    override fun onActivityStarted(activity: Activity?) {
        notifyObservers(Notification(ACTIVITY_STARTED, activity))
    }

    override fun onActivityResumed(activity: Activity?) {
        notifyObservers(Notification(ACTIVITY_RESUMED, activity))
    }

    override fun onActivityPaused(activity: Activity?) {
        notifyObservers(Notification(ACTIVITY_PAUSED, activity))
    }

    override fun onActivityStopped(activity: Activity) {
        notifyObservers(Notification(ACTIVITY_STOPPED, activity))
        // if activity is finish there work we must clear the view
        // and all references to recreate view state manually when activity is wake up
        if(this.currentActivity?.get()?.isFinishing == true) detachActivity()
    }

    override fun onActivityDestroyed(activity: Activity?) {
        notifyObservers(Notification(ACTIVITY_DESTROYED, activity))
        if(this.currentActivity?.get() == activity) detachActivity()
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
        bundle.putBundle(STATE_BUNDLE_KEY, stateBundle)
        notifyObservers(Notification(ACTIVITY_STATE_SAVE, arguments))
        // only when activity change there configuration state ex rotate
        if(this.currentActivity?.get()?.isChangingConfigurations == true) detachActivity()
    }

    /**
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
     * Private access to clear view on IMediator instance and follow mediator lifecyrcle
     */
    override fun clearMediatorView(mediator: IMediator?) {
        mediator?.apply {
            (viewComponent as? ViewGroup)?.clear()
            viewComponent = null
            isDestroyed = true
            onDestroyView()
        }
    }

    init {
        arguments = Bundle()
    }
}