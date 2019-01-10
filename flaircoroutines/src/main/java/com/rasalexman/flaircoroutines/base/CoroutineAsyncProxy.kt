package com.rasalexman.flaircoroutines.base

import com.rasalexman.flaircoroutines.managers.CoroutinesManager
import com.rasalexman.flaircoroutines.managers.ICoroutinesManager

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
open class CoroutineAsyncProxy<T>(override var data: T? = null, private val coroutinesManager: CoroutinesManager = CoroutinesManager()) : AsyncProxy<T>(data), ICoroutinesManager by coroutinesManager