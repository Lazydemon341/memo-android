package com.memo.memories.internal.data.network

import com.memo.core.network.retrofit.ApiFactory
import com.memo.memories.internal.data.network.model.MemoryResponse
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

internal interface MemoryApi {

    @GET("memory/get")
    suspend fun getMemory(@Query("id") memoryId: Long?): MemoryResponse
}

@Singleton
internal class NetworkMemoryDataSource @Inject constructor(
    apiFactory: ApiFactory,
) {

    private val api = apiFactory.create(MemoryApi::class.java)

    suspend fun getMemory(memoryId: Long): MemoryResponse {
        return api.getMemory(memoryId = memoryId)
    }
}
