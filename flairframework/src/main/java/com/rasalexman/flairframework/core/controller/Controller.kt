package com.rasalexman.flairframework.core.controller

import com.rasalexman.flairframework.core.view.View
import com.rasalexman.flairframework.interfaces.ICommand
import com.rasalexman.flairframework.interfaces.IController
import com.rasalexman.flairframework.interfaces.IMapper
import com.rasalexman.flairframework.interfaces.instance

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
    override val commandMap: MutableMap<String, ICommand?> = mutableMapOf()

    /**
     * Local reference to View
     */
    override val view: View by lazy { View.getInstance(multitonKey) }

    /**
     * Singleton companion object to take and store references for Command pattern
     */
    companion object : IMapper<Controller> {
        /**
         * Global storage for all instances of Controller
         */
        override val instanceMap: MutableMap<String, Controller> = mutableMapOf()
        /**
         * `Controller` Multiton Factory method.
         * @return the Multiton core of `Controller` or create new if not exist
         */
        @Synchronized
        fun getInstance(key: String) = instance(key) { Controller(key) }

        /**
         * Remove an IController core
         * @param key of IController core to remove
         */
        @Synchronized
        fun removeController(key: String) {
            instanceMap.remove(key)?.clearAll()
        }
    }

    /**
     * Clear all reference to commands
     */
    private fun clearAll() {
        commandMap.clear()
    }
}