package com.memo.core.network

import com.memo.core.network.model.widgets.PhotoWidgetResponse

interface NetworkWidgetDataSource {

    suspend fun getLastPhoto(): PhotoWidgetResponse
}
