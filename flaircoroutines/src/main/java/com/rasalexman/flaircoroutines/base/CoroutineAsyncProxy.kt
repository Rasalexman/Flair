package com.rasalexman.flaircoroutines.base

import com.rasalexman.coroutinesmanager.CancelationHandler
import com.rasalexman.coroutinesmanager.ICoroutinesManager

/**
 * Base Proxy Coroutines class
 * Used for launchOnUI{} with async{} block calls
 *
 * @param data
 * Main data to storage
 *
 * @param coroutinesManager
 * Any CoroutinesManager instance. You can use di for this
 */
open class CoroutineAsyncProxy<T>(override var data: T? = null) : AsyncProxy<T>(data), ICoroutinesManager {
    override val cancelationHandlers: MutableSet<CancelationHandler> = mutableSetOf()
}