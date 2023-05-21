package com.memo.core.model

fun interface Factory<T> {
    fun create(): T
}
