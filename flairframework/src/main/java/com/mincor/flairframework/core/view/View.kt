package com.mincor.flairframework.core.view

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import com.mincor.flairframework.core.FlairActivity
import com.mincor.flairframework.interfaces.*
import com.mincor.flairframework.patterns.observer.Notification
import java.util.*


/**
 * Created by a.minkin on 21.11.2017.
 */
class View : Fragment(), IView, Application.ActivityLifecycleCallbacks {

    override var multitonKey: String = ""

    // Mapping of Notification names to Observer lists
    override val observerMap: MutableMap<String, MutableList<IObserver>> = mutableMapOf()
    // Mapping of Mediator names to Mediator instances
    override val mediatorMap: MutableMap<String, IMediator> = mutableMapOf()

    // List of current added mediators on the screen
    override val mediatorBackStack: MutableList<IMediator> = mutableListOf()
    // Current showing mediatorLazy
    override var currentShowingMediator: IMediator? = null

    /**
     * Reference to the Activity attached on core
     */
    override var currentActivity: FlairActivity? = null
    /**
     * Instance of ui container
     */
    override var currentContainer: ViewGroup? = null

    /**
     * is already registered lifecycle callbacks
     */
    private var isAlreadyRegistered: Boolean = false


    companion object : IMapper<View> {
        const val ACTIVITY_CREATED = "created"
        const val ACTIVITY_STARTED = "started"
        const val ACTIVITY_RESUMED = "resumed"
        const val ACTIVITY_PAUSED = "paused"
        const val ACTIVITY_STOPPED = "stopped"
        const val ACTIVITY_DESTROYED = "destroyed"
        const val ACTIVITY_STATE_SAVE = "state_save"

        override val instanceMap: MutableMap<String, View> = mutableMapOf()
        /**
         * View Singleton Factory method.
         *
         * @return the Singleton core of `View`
         */
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
        mediatorMap.forEach { _, iMediator ->
            iMediator.hide(null, true)
        }
        mediatorMap.clear()
        mediatorBackStack.clear()
        currentShowingMediator = null

        currentContainer?.removeAllViews()
        currentContainer = null

        currentActivity?.application?.registerActivityLifecycleCallbacks(null)
        currentActivity = null

    }

    /**
     * Attach current activity to the core view
     * Only one activity can be attached to the core
     *
     * @param activity
     * Current activity to be attached with lifecycle
     *
     * @param container
     * The container when ui will be added if there no container we take default activity decorView content (frame layout)
     */
    override fun attachActivity(activity: FlairActivity, container: ViewGroup?) {
        //this is because we need container to add views anyway
        currentContainer = container ?: activity.window.decorView.findViewById(android.R.id.content)
        if (!isAlreadyRegistered) {
            currentActivity = activity
            val fragmentManager:FragmentManager? = (activity as? AppCompatActivity)?.supportFragmentManager
            fragmentManager?.beginTransaction()?.replace(android.R.id.content, this, multitonKey)?.commit() //
            activity.application.registerActivityLifecycleCallbacks(this)
            isAlreadyRegistered = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        currentShowingMediator?.let {
            if (it.hasOptionalMenu) {
                it.onCreateOptionsMenu(menu, inflater)
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        currentShowingMediator?.let {
            if (it.hasOptionalMenu) {
                it.onPrepareOptionsMenu(menu)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (currentShowingMediator?.onOptionsItemSelected(item) == true) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //-------------- LIFE CYCLE CALLBACKS -------/////
    override fun onActivityCreated(activity: Activity?, p1: Bundle?) {
        notifyObservers(Notification(ACTIVITY_STARTED, activity))
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

    override fun onActivityStopped(activity: Activity?) {
        notifyObservers(Notification(ACTIVITY_STOPPED, activity))
    }

    override fun onActivityDestroyed(activity: Activity?) {
        notifyObservers(Notification(ACTIVITY_DESTROYED, activity))
        currentContainer?.removeAllViews()
        currentContainer = null
        currentShowingMediator = null
    }

    override fun onActivitySaveInstanceState(activity: Activity?, bundle: Bundle?) {
        notifyObservers(Notification(ACTIVITY_STATE_SAVE, bundle))
    }
    /////////------------------------------------///////


    init {
        arguments = Bundle()
    }
}