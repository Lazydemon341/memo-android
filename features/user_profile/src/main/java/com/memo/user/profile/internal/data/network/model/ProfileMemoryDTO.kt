package com.memo.user.profile.internal.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class ProfileMemoryDTO(
    @Json(name = "id")
    val id: Long,

    @Json(name = "name")
    val name: String,

    @Json(name = "photos_preview_url")
    val photoPreviewUrl: String,
)
