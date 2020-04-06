package com.rasalexman.flaircore.interfaces

/**
 * Helper interface for bind generic instances with name
 */
interface IMapper<T> {
    /**
     * Instances storage
     */
    val instanceMap: HashMap<String, T>

    /**
     * Get or create instance
     *
     * @param key - key for map key
     * @param inst - instance for create as value
     */
    fun <T> IMapper<T>.instance(key: String, inst: () -> T): T = this.instanceMap.getOrPut(key) { inst() }
}
