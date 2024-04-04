package com.pedfu.daystreak.data.remote.signup

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SignupRequest(
    @Json(name = "email") val email: String,
    @Json(name = "username") val username: String,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
    @Json(name = "password") val password: String
)