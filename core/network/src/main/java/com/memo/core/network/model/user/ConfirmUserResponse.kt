package com.memo.core.network.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ConfirmUserResponse(
    @Json(name = "confirmed")
    val isConfirmed: Boolean,
)
