package com.rasalexman.flaircoroutines.managers

import com.rasalexman.flaircoroutines.utils.CoroutinesProvider
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * AsyncTasksManager for doing some async work
 */
open class AsyncTasksManager : IAsyncTasksManager, CoroutineScope {

    /**
     * Async Job
     */
    private val job = SupervisorJob()

    /**
     * CoroutineContext to use in this manager. It's async
     */
    override val coroutineContext: CoroutineContext
        get() = CoroutinesProvider.COMMON + job

    /**
     * launch coroutine on common pool job
     *
     * @param block
     * The worker block to invoke
     */
    @Synchronized
    override suspend fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return async(CoroutinesProvider.COMMON) { block() }.also { job -> job.invokeOnCompletion { job.cancel() } }
    }

    /**
     * launch coroutine on common pool job
     *
     * @param block
     * The worker block to invoke
     */
    @Synchronized
    override suspend fun <T> asyncAwait(block: suspend CoroutineScope.() -> T): T {
        return async(block).await()
    }

    /**
     * cancel all working coroutines
     */
    @Synchronized
    override fun cancelAllAsync() {
        coroutineContext.cancelChildren()
    }

    /**
     * Clear all working coroutines
     */
    @Synchronized
    override fun cleanup() {
        cancelAllAsync()
    }
}