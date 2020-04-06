package com.rasalexman.flaircore.patterns.facade

import android.content.Context
import com.rasalexman.flaircore.controller.Controller
import com.rasalexman.flaircore.interfaces.IFacade
import com.rasalexman.flaircore.model.Model
import com.rasalexman.flaircore.view.View
import java.lang.ref.WeakReference


/**
 * Created by a.minkin on 21.11.2017.
 *
 * Main Facade application class
 *
 * @param multitonKey - main instance key
 * @param appContext - application [Context]
 */
class Facade(
        override var multitonKey: String,
        override val appContext: WeakReference<Context>
) : IFacade {

    /**
     * Lazy IFacade instance
     */
    override val facade: IFacade by lazy { this }

    /**
     * The Multiton core of `Controller` or create new if not exist
     */
    override val controller by lazy { Controller.getInstance(multitonKey) }

    /**
     * Reference to the Model
     */
    override val model by lazy { Model.getInstance(multitonKey) }

    /**
     * Reference to the View
     */
    override val view by lazy { View.getInstance(multitonKey) }
}