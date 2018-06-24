package com.mincor.flairframework.patterns.facade

import android.content.Context
import android.view.ViewGroup
import com.mincor.flairframework.core.FlairActivity
import com.mincor.flairframework.core.controller.Controller
import com.mincor.flairframework.core.model.Model
import com.mincor.flairframework.core.view.View
import com.mincor.flairframework.interfaces.FacadeInitializer
import com.mincor.flairframework.interfaces.IFacade


/**
 * Created by a.minkin on 21.11.2017.
 */
class Facade(override var multitonKey: String = "",
             override val appContext: Context,
             facadeInitializer:FacadeInitializer? = null) : IFacade {

    /**
     * Lazy IFacade instance
     */
    override val facade: IFacade by lazy { this }

    /**
     * Reference to the Controller
     */
    override val controller by lazy { Controller.getInstance(multitonKey)  }

    /**
     * Reference to the Model
     */
    override val model by lazy { Model.getInstance(multitonKey) }

    /**
     * Reference to the View
     */
    override val view by lazy { View.getInstance(multitonKey) }

    init {
        facadeInitializer?.let { it() }
    }

    /**
     * Attach current activity and parent container to this core
     * only one core can has one activity to attach, we cant reattach activity to the core
     *
     * @param activity
     * Current Activity to attach the core
     *
     * @param container
     * Current container (ViewGroup) to add childs viewComponents from Mediators
     */
    override fun attach(activity: FlairActivity, container: ViewGroup?): IFacade {
        view.attachActivity(activity, container)
        return this
    }
}