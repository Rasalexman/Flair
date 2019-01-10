package com.rasalexman.flaircoroutines.managers

import kotlinx.coroutines.CoroutineScope

/**
 * ICoroutinesManager
 */
interface ICoroutinesManager {

    /**
     * Launch some suspend function on UI thread
     */
    fun launchOnUI(block: suspend CoroutineScope.() -> Unit)

    /**
     * Launch some suspend function on UI thread with try catch block
     */
    fun launchOnUITryCatch(
            tryBlock: suspend CoroutineScope.() -> Unit,
            catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
            handleCancellationExceptionManually: Boolean = false)

    /**
     * Launch some suspend function on UI thread with try catch finally block
     */
    fun launchOnUITryCatchFinally(
            tryBlock: suspend CoroutineScope.() -> Unit,
            catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
            finallyBlock: suspend CoroutineScope.() -> Unit,
            handleCancellationExceptionManually: Boolean = false)

    /**
     * Launch some suspend function on UI thread with try finally block
     */
    fun launchOnUITryFinally(
            tryBlock: suspend CoroutineScope.() -> Unit,
            finallyBlock: suspend CoroutineScope.() -> Unit,
            suppressCancellationException: Boolean = false)

    /**
     * Cancel all working coroutines
     */
    fun cancelAllCoroutines()
}