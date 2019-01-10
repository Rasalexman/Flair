package com.rasalexman.flaircoroutines.managers

import com.rasalexman.flaircoroutines.utils.CoroutinesProvider
import com.rasalexman.flaircoroutines.utils.tryCatch
import com.rasalexman.flaircoroutines.utils.tryCatchFinally
import com.rasalexman.flaircoroutines.utils.tryFinally
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Simple CoroutinesManager to use with your regular classes
 */
open class CoroutinesManager : ICoroutinesManager, CoroutineScope {

    /**
     * Working job
     */
    private val job = SupervisorJob()

    /**
     * CoroutineContext UI thread
     */
    override val coroutineContext: CoroutineContext
        get() = CoroutinesProvider.UI + job

    /**
     * launch single coroutine job on main thread
     */
    @Synchronized
    override fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        launch(CoroutinesProvider.UI) { block() }.also { job -> job.invokeOnCompletion { job.cancel() } }
    }

    /**
     * launch single coroutine job on main thread with try catch block
     */
    @Synchronized
    override fun launchOnUITryCatch(
            tryBlock: suspend CoroutineScope.() -> Unit,
            catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
            handleCancellationExceptionManually: Boolean) {
        launchOnUI { tryCatch(tryBlock, catchBlock, handleCancellationExceptionManually) }
    }

    /**
     * launch single coroutine job on main thread with try catch and finally block
     */
    @Synchronized
    override fun launchOnUITryCatchFinally(
            tryBlock: suspend CoroutineScope.() -> Unit,
            catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
            finallyBlock: suspend CoroutineScope.() -> Unit,
            handleCancellationExceptionManually: Boolean) {
        launchOnUI { tryCatchFinally(tryBlock, catchBlock, finallyBlock, handleCancellationExceptionManually) }
    }

    /**
     * launch single coroutine job on main thread with try and finally block
     */
    @Synchronized
    override fun launchOnUITryFinally(
            tryBlock: suspend CoroutineScope.() -> Unit,
            finallyBlock: suspend CoroutineScope.() -> Unit,
            suppressCancellationException: Boolean) {
        launchOnUI { tryFinally(tryBlock, finallyBlock, suppressCancellationException) }
    }

    /**
     * Cancel all working coroutines
     */
    @Synchronized
    override fun cancelAllCoroutines() {
        coroutineContext.cancelChildren()
    }
}