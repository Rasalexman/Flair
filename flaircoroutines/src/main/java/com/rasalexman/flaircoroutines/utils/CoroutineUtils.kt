package com.rasalexman.flaircoroutines.utils

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope

/**
 * CoroutineScope.tryCatch for useful
 *
 * @param tryBlock
 * main try block
 *
 * @param catchBlock
 * main catch block
 *
 * @param handleCancellationExceptionManually
 * Does we need to handle exception with our catch block. Default false
 */
suspend fun CoroutineScope.tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        handleCancellationExceptionManually: Boolean = false) {
    try {
        tryBlock()
    } catch (e: Throwable) {
        if (e !is CancellationException ||
                handleCancellationExceptionManually) {
            catchBlock(e)
        } else {
            throw e
        }
    }
}

/**
 * CoroutineScope.tryCatchFinally for useful
 *
 * @param tryBlock
 * main try block
 *
 * @param catchBlock
 * main catch block
 *
 * @param finallyBlock
 * main final block
 *
 * @param handleCancellationExceptionManually
 * Does we need to handle exception with our catch block. Default false
 */
suspend fun CoroutineScope.tryCatchFinally(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false) {

    var caughtThrowable: Throwable? = null

    try {
        tryBlock()
    } catch (e: Throwable) {
        if (e !is CancellationException ||
                handleCancellationExceptionManually) {
            catchBlock(e)
        } else {
            caughtThrowable = e
        }
    } finally {
        if (caughtThrowable is CancellationException &&
                !handleCancellationExceptionManually) {
            throw caughtThrowable
        } else {
            finallyBlock()
        }
    }
}


/**
 * CoroutineScope.tryFinally for useful
 *
 * @param tryBlock
 * main try block
 *
 * @param finallyBlock
 * main catch block
 *
 * @param suppressCancellationException
 * Default false
 */
suspend fun CoroutineScope.tryFinally(
        tryBlock: suspend CoroutineScope.() -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        suppressCancellationException: Boolean = false) {

    var caughtThrowable: Throwable? = null

    try {
        tryBlock()
    } catch (e: CancellationException) {
        if (!suppressCancellationException) {
            caughtThrowable = e
        }
    } finally {
        if (caughtThrowable is CancellationException &&
                !suppressCancellationException) {
            throw caughtThrowable
        } else {
            finallyBlock()
        }
    }
}