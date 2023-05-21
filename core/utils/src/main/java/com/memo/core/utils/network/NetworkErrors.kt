package com.memo.core.utils.network

import retrofit2.HttpException
import java.io.IOException
import javax.net.ssl.SSLException

fun Throwable?.isNetworkOrServerError(): Boolean {
    return isNetworkError() || isServerError()
}

fun Throwable?.isNetworkError(): Boolean {
    return this is IOException && this !is SSLException
}

fun Throwable?.isServerError(): Boolean {
    if (this is HttpException) {
        return code() / 100 == 5
    }
    return false
}
