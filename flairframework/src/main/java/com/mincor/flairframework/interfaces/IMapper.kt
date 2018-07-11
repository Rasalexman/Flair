package com.mincor.flairframework.interfaces

/**
 * Helper interface for bind generic instances with name
 */
interface IMapper<T> {
    val instanceMap: MutableMap<String, T>
}
inline fun <reified T> IMapper<T>.instance(key:String, inst:()->T):T = this.instanceMap.getOrPut(key) { inst() }
