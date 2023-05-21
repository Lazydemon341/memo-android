package com.memo.core.network.model.messaging

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MessageSenderDTO(
    @Json(name = "email")
    val email: String,

    @Json(name = "photo_url")
    val photoUrl: String?,
)
