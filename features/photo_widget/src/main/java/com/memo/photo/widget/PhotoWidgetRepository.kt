package com.memo.photo.widget

import com.memo.photo.widget.models.LastPhotoInfo

interface PhotoWidgetRepository {

    suspend fun getLastPhoto(): Result<LastPhotoInfo>
}
