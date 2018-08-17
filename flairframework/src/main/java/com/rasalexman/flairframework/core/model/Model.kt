package com.rasalexman.flairframework.core.model

import android.util.ArrayMap
import com.rasalexman.flairframework.interfaces.IMapper
import com.rasalexman.flairframework.interfaces.IModel
import com.rasalexman.flairframework.interfaces.IProxy
import com.rasalexman.flairframework.interfaces.instance

/**
 * Created by a.minkin on 21.11.2017.
 */
open class Model private constructor(override var multitonKey: String) : IModel {
    /**
     * Mapping of proxyNames to IProxy instances.
     */
    override val proxyMap = ArrayMap<String, IProxy<*>>()

    companion object : IMapper<Model> {
        override val instanceMap = ArrayMap<String, Model>()
        /**
         * `Model` Multiton Factory method.
         *
         * @return the core for this Multiton key
         */
        @Synchronized
        fun getInstance(key: String): IModel = instance(key){ Model(key) }

        /**
         * Remove an IModel core
         *
         * @param key of IModel core to remove
         */
        @Synchronized
        fun removeModel(key: String) {
            instanceMap.remove(key)?.clearAll()
        }
    }

    private fun clearAll() {
        proxyMap.clear()
    }
}