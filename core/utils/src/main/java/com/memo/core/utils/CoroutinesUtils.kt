package com.memo.core.utils

import kotlin.coroutines.cancellation.CancellationException

inline fun <T> suspendRunCatching(block: () -> T): Result<T> = try {
    Result.success(block())
} catch (cancellationException: CancellationException) {
    throw cancellationException
} catch (throwable: Throwable) {
    Result.failure(throwable)
}

inline fun <R> trySuspend(
    run: () -> R,
    catch: (throwable: Throwable) -> R,
    finally: () -> Unit = {},
): R = try {
    run()
} catch (throwable: Throwable) {
    if (throwable is CancellationException) {
        throw throwable
    }
    catch(throwable)
} finally {
    finally()
}
