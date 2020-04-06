package com.rasalexman.flaircoroutines.base

import com.rasalexman.coroutinesmanager.AsyncTasksManager
import com.rasalexman.coroutinesmanager.IAsyncTasksManager
import com.rasalexman.flaircore.patterns.proxy.Proxy

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
open class AsyncProxy<T>(override var data: T, private val asyncTasksManager: AsyncTasksManager = AsyncTasksManager()) : Proxy<T>(data), IAsyncTasksManager by asyncTasksManager