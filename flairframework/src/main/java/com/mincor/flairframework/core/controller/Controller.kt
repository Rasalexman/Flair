package com.mincor.flairframework.core.controller

import com.mincor.flairframework.core.view.View
import com.mincor.flairframework.interfaces.ICommand
import com.mincor.flairframework.interfaces.IController
import com.mincor.flairframework.interfaces.IMapper
import com.mincor.flairframework.interfaces.instance

class Controller private constructor(override var multitonKey: String) : IController {

    /**
     * Mapping of Notification names to Command Class references
     */
    override val commandMap: MutableMap<String, ICommand?> = mutableMapOf()

    /**
     * Local reference to View
     */
    override val view: View by lazy { View.getInstance(multitonKey) }

    companion object : IMapper<Controller> {
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

    private fun clearAll() {
        commandMap.clear()
    }
}