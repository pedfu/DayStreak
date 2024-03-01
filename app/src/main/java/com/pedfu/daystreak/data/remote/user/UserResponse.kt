package com.pedfu.daystreak.data.remote.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UserResponse (
    @Json(name = "id") val id: Long,
    @Json(name = "email") val email: String,
    @Json(name = "username") val username: String?,
    @Json(name = "uid") val uid: String?,
    @Json(name = "photoUrl") val photoUrl: String?,
)