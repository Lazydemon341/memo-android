package com.memo.core.network.model.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PostUploadParams(
    @Json(name = "photo")
    val photo: PhotoDTO,

    @Json(name = "memory_id")
    val memoryId: Long?,

    @Json(name = "caption")
    val caption: String?,

    @Json(name = "people_to_send")
    val peopleToSend: List<Long>?,
)
