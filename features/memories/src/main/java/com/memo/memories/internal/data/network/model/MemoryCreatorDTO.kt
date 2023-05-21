package com.memo.memories.internal.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class MemoryCreatorDTO(
    @Json(name = "id")
    val id: Long,

    @Json(name = "name")
    val name: String,

    @Json(name = "surname")
    val surname: String,

    @Json(name = "email")
    val email: String,
)
