package com.memo.core.network

import com.memo.core.network.model.memory.MemoryCreateRequestBody
import com.memo.core.network.model.memory.MemoryCreateResponse

interface NetworkMemoryDataSource {

    suspend fun uploadMemory(memory: MemoryCreateRequestBody): MemoryCreateResponse
}
