package com.rasalexman.flaircoroutines.base

import com.rasalexman.flaircore.patterns.proxy.Proxy
import com.rasalexman.flaircoroutines.managers.AsyncTasksManager
import com.rasalexman.flaircoroutines.managers.IAsyncTasksManager

/**
 * Base Proxy Coroutines Async class
 * Used for async {} block calls
 *
 * @param data
 * Main data to storage
 *
 * @param asyncTasksManager
 * Any AsyncTaskManager instance. You can use di for this
 */
open class AsyncProxy<T>(override var data: T? = null, private val asyncTasksManager: AsyncTasksManager = AsyncTasksManager()) : Proxy<T>(data), IAsyncTasksManager by asyncTasksManager