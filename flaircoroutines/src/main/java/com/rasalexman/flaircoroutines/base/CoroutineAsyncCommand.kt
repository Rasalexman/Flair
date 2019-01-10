package com.rasalexman.flaircoroutines.base

import com.rasalexman.flaircoroutines.managers.AsyncTasksManager
import com.rasalexman.flaircoroutines.managers.IAsyncTasksManager

/**
 * Base Command Async class
 * Used for async{} block calls
 *
 * @param asyncTasksManager
 * Any AsyncTasksManager instance. You can use di for this
 */
abstract class CoroutineAsyncCommand(private val asyncTasksManager: AsyncTasksManager = AsyncTasksManager()) : CoroutineCommand(), IAsyncTasksManager by asyncTasksManager