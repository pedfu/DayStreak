package com.pedfu.daystreak.data.remote.setting

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ProfilePictureResponse (
    @Json(name = "profile_picture_path") val profilePicturePath: String?,
)