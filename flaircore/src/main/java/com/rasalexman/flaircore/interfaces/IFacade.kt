package com.rasalexman.flaircore.interfaces

import android.app.Activity
import android.content.Context
import androidx.collection.ArrayMap
import com.rasalexman.flaircore.controller.Controller
import com.rasalexman.flaircore.ext.FacadeInitializer
import com.rasalexman.flaircore.model.Model
import com.rasalexman.flaircore.patterns.facade.Facade
import com.rasalexman.flaircore.view.View
import java.lang.ref.WeakReference

/**
 * Created by a.minkin on 21.11.2017.
 */
interface IFacade : INotifier {

    /**
     * Application context
     */
    val appContext: WeakReference<Context>
    /**
     * Controller instance
     */
    val controller: IController
    /**
     * Model instance
     */
    val model: IModel
    /**
     * View instance
     */
    val view: IView

    /**
     * Static object holder
     */
    companion object : IMapper<IFacade> {
        /**
         * Default key for single facade core
         */
        const val DEFAULT_KEY = "DEFAULT_FACADE"
        /**
         * Global storage for all instance cores of IFacade
         */
        override val instanceMap by lazy {  ArrayMap<String, IFacade>() }

        /**
         * Facade Multiton Factory method.
         *
         * @param key
         * the name and multiton key for this IFacade core
         *
         * @param context
         * Application or Activity main classes
         *
         * @param init
         * init function for this core
         *
         * @return the Multiton core of the Facade
         */
        @Throws
        fun core(key: String = DEFAULT_KEY, context: Context? = null, init: FacadeInitializer? = null): IFacade {
            val facade = instance(key) {
                if (context == null) throw RuntimeException("You need to specified `context` for this core")
                Facade(key, WeakReference(if(context is Activity) context.applicationContext else context))
            }
            init?.invoke(facade)
            return facade
        }

        /**
         * Check if a Core is registered or not.
         *
         * @param key the multiton key for the Core in question
         * @return whether a Core is registered with the given `key`.
         */
        @Synchronized
        fun hasCore(key: String = DEFAULT_KEY): Boolean = instanceMap.containsKey(key)

        /**
         * Remove a Core
         * @param key of the Core to remove
         */
        @Synchronized
        fun removeCore(key: String = DEFAULT_KEY) {
            Model.removeModel(key)
            View.removeView(key)
            Controller.removeController(key)
            instanceMap.remove(key)?.clearAll()
        }

        /**
         * Clear all reference from core
         */
        private fun IFacade.clearAll() {
            // remove the model, view, controller
            // and facade instances for this key
            appContext.clear()
        }
    }
}