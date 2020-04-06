package com.rasalexman.flaircore.controller

import com.rasalexman.flaircore.interfaces.ICommand
import com.rasalexman.flaircore.interfaces.IController
import com.rasalexman.flaircore.interfaces.IMapper
import com.rasalexman.flaircore.view.View

/**
 * This is a main Controller core private class for registering commands
 *
 * @param multitonKey
 * The key for associated with core IFacade instance
 */
class Controller private constructor(override var multitonKey: String) : IController {

    /**
     * Mapping of Notification names to Command Class references
     */
    override val commandMap by lazy { HashMap<String, ICommand?>() }

    /**
     * Local reference to View
     */
    override val view by lazy { View.getInstance(multitonKey) }

    /**
     * Singleton companion object to take and store references for Command pattern
     */
    companion object : IMapper<Controller> {
        /**
         * Global storage for all instances of Controller
         */
        override val instanceMap by lazy { HashMap<String, Controller>() }
        /**
         * `Controller` Multiton Factory method.
         * @return the Multiton core of `Controller` or create new if not exist
         */
        @Synchronized
        fun getInstance(key: String): IController = instance(key) { Controller(key) }

        /**
         * Remove an IController core
         * @param key of IController core to remove
         */
        @Synchronized
        fun removeController(key: String) {
            instanceMap.remove(key)?.clearAll()
        }

        /**
         * Clear all reference to commands
         */
        private fun IController.clearAll() {
            commandMap.clear()
        }
    }
}