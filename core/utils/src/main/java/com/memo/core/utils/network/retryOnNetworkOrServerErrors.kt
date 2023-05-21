package com.memo.core.utils.network

import kotlinx.coroutines.delay

tailrec suspend fun <T> retryOnNetworkOrServerErrors(
    maxRetry: Int = 3,
    delayMs: Long = 2_000,
    block: suspend () -> T,
): T {
    try {
        return block.invoke()
    } catch (e: Exception) {
        when {
            maxRetry <= 0 -> {
                throw e
            }
            e.isNetworkOrServerError() -> {
                delay(delayMs)
            }
            else -> {
                throw e
            }
        }
    }
    return retryOnNetworkOrServerErrors(maxRetry - 1, delayMs, block)
}
