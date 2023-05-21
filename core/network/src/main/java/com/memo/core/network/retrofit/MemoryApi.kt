package com.memo.core.network.retrofit

import com.memo.core.network.NetworkMemoryDataSource
import com.memo.core.network.model.memory.MemoryCreateRequestBody
import com.memo.core.network.model.memory.MemoryCreateResponse
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

interface MemoryApi {

    @POST("memory/upload")
    suspend fun upload(@Body requestBody: MemoryCreateRequestBody): MemoryCreateResponse
}

@Singleton
class RetrofitMemoryDataSource @Inject constructor(
    apiFactory: ApiFactory,
) : NetworkMemoryDataSource {

    private val api = apiFactory.create(MemoryApi::class.java)

    override suspend fun uploadMemory(memory: MemoryCreateRequestBody): MemoryCreateResponse {
        return api.upload(memory)
    }
}
