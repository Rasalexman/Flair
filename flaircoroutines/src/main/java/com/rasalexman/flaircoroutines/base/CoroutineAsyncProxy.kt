package com.rasalexman.flaircoroutines.base

import com.rasalexman.coroutinesmanager.CancelationHandler
import com.rasalexman.coroutinesmanager.ICoroutinesManager

/**
 * Base Proxy Coroutines class
 * Used for launchOnUI{} with async{} block calls
 *
 * @param data
 * Main data to storage
 */
open class CoroutineAsyncProxy<T>(override var data: T) : AsyncProxy<T>(data), ICoroutinesManager {
    /**
     * Coroutine cancelation Handlers set
     */
    override val cancelationHandlers: MutableSet<CancelationHandler> = mutableSetOf()
}