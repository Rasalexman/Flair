package com.rasalexman.flaircore.interfaces

import androidx.collection.ArrayMap

/**
 * Helper interface for bind generic instances with name
 */
interface IMapper<T> {
    /**
     * Instances storage
     */
    val instanceMap: ArrayMap<String, T>
}

/**
 * Get or create instance
 *
 * @param key - key for map key
 * @param inst - instance for create as value
 */
inline fun <reified T> IMapper<T>.instance(key: String, inst: () -> T): T = this.instanceMap.getOrPut(key) { inst() }
