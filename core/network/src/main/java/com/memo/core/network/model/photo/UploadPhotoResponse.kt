package com.memo.core.network.model.photo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UploadPhotoResponse(
    @Json(name = "photos_id")
    val photosIds: List<Long>,
)
