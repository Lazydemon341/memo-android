package com.memo.core.network.retrofit

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

private const val baseUrl = "https://memo-app.develop.sprinthub.ru/api/"

@Singleton
class ApiFactory @Inject constructor(
    private val moshi: Moshi,
    private val okHttpClient: OkHttpClient,
) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    fun <T> create(apiClass: Class<T>): T {
        return retrofit.create(apiClass)
    }
}
