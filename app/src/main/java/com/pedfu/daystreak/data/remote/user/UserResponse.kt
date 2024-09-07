package com.pedfu.daystreak.data.remote.user

import android.net.Uri
import com.pedfu.daystreak.domain.user.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UserResponse(
    @Json(name = "id") val id: Long,
    @Json(name = "email") val email: String,
    @Json(name = "first_name") val firstName: String?,
    @Json(name = "last_name") val lastName: String?,
    @Json(name = "username") val username: String?,
    @Json(name = "role") val role: String?,
    @Json(name = "uuid") val uuid: String?,
    @Json(name = "maxStreak") val maxStreak: Int?,
    @Json(name = "photoUrl") val photoUrl: String?,
) {
    fun toUser(): User {
        return User(
            id,
            email,
            firstName ?: "",
            lastName ?: "",
            username ?: "",
            role ?: "",
            if (photoUrl != null) Uri.parse(photoUrl) else null,
            uuid  ?: "",
            maxStreak  ?: 0
        )
    }
}