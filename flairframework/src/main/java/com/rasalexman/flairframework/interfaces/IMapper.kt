package com.rasalexman.flairframework.interfaces

import android.util.ArrayMap

/**
 * Helper interface for bind generic instances with name
 */
interface IMapper<T> {
    val instanceMap: ArrayMap<String, T>
}
inline fun <reified T> IMapper<T>.instance(key:String, inst:()->T):T = this.instanceMap.getOrPut(key) { inst() }
