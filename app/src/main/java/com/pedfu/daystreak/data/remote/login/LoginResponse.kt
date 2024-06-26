package com.pedfu.daystreak.data.remote.login

import com.pedfu.daystreak.data.remote.user.UserJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class LoginResponse(
    @Json(name = "key") val tokenKey: String,
    @Json(name = "user") val user: UserJson
)