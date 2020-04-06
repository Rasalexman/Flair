package com.rasalexman.flaircore.interfaces

/**
 * Created by a.minkin on 21.11.2017.
 */
interface IModel : IMultitonKey {
    /**
     * Main [IProxy] storage for [IFacade] core
     */
    val proxyMap: HashMap<String, IProxy<*>>
}