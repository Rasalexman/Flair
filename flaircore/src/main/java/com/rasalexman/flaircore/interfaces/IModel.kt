package com.rasalexman.flaircore.interfaces

import androidx.collection.ArrayMap

/**
 * Created by a.minkin on 21.11.2017.
 */
interface IModel : IMultitonKey {
    /**
     * Main [IProxy] storage for [IFacade] core
     */
    val proxyMap: ArrayMap<String, IProxy<*>>
}