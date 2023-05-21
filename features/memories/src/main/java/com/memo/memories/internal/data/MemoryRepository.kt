package com.memo.memories.internal.data

import com.memo.core.utils.network.retryOnNetworkOrServerErrors
import com.memo.core.utils.suspendRunCatching
import com.memo.memories.internal.data.network.NetworkMemoryDataSource
import com.memo.memories.internal.data.network.model.MemoryResponse
import javax.inject.Inject

internal class MemoryRepository @Inject constructor(
    private val networkMemoryDataSource: NetworkMemoryDataSource,
) {

    suspend fun getMemory(memoryId: Long): Result<MemoryResponse> {
        return suspendRunCatching {
            retryOnNetworkOrServerErrors {
                networkMemoryDataSource.getMemory(memoryId = memoryId)
            }
        }
    }
}
