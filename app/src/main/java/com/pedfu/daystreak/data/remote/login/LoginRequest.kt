package com.pedfu.daystreak.data.remote.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class LoginRequest(
    @Json(name = "username_or_email") val usernameOrEmail: String,
    @Json(name = "password") val password: String
)