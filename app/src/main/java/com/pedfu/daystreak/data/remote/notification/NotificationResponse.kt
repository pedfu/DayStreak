package com.pedfu.daystreak.data.remote.notification

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
class NotificationResponse (
    @Json(name = "id") val id: Long,
    @Json(name = "main_message") val mainMessage: String,
    @Json(name = "secondary_message") val secondaryMessage: String,
    @Json(name = "type") val type: String,
    @Json(name = "read") val read: Boolean,
    @Json(name = "created_at") val createdAt: Date, // for offline update
    @Json(name = "badge") val badge: BadgeResponse,
)