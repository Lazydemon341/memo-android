package com.memo.core.network.model.widgets

import com.memo.core.network.model.common.LocationDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PhotoWidgetResponse(
    @Json(name = "post_image")
    val postImageUrl: String,

    @Json(name = "location")
    val location: LocationDTO?,

    @Json(name = "user")
    val user: PhotoWidgetUserModel,

    @Json(name = "caption")
    val caption: String?,
)
