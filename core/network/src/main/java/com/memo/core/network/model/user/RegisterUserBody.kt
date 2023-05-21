package com.memo.core.network.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RegisterUserBody(
    @Json(name = "name")
    val name: String,

    @Json(name = "surname")
    val surname: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "password")
    val password: String,
)
