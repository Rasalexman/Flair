package com.rasalexman.flaircore.model

import com.rasalexman.flaircore.interfaces.IMapper
import com.rasalexman.flaircore.interfaces.IModel
import com.rasalexman.flaircore.interfaces.IProxy

/**
 * Created by a.minkin on 21.11.2017.
 *
 * Main IModel core class that used for [IProxy] objects manipulating
 *
 * @param multitonKey - main core key that this model sequenced
 */
class Model private constructor(override var multitonKey: String) : IModel {
    /**
     * Mapping of proxyNames to IProxy instances.
     */
    override val proxyMap by lazy { HashMap<String, IProxy<*>>() }

    /**
     * CO for instances creation
     */
    companion object : IMapper<Model> {

        /**
         * Model Instances storage
         */
        override val instanceMap by lazy { HashMap<String, Model>() }

        /**
         * `Model` Multiton Factory method.
         *
         * @return the core for this Multiton key
         */
        @Synchronized
        fun getInstance(key: String): IModel = instance(key) { Model(key) }

        /**
         * Remove an IModel core
         *
         * @param key of IModel core to remove
         */
        @Synchronized
        fun removeModel(key: String) {
            instanceMap.remove(key)?.clearAll()
        }

        /**
         * Clear storage
         */
        private fun IModel.clearAll() {
            proxyMap.clear()
        }
    }
}