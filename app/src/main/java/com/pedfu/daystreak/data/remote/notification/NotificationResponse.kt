package com.pedfu.daystreak.data.remote.notification

import com.pedfu.daystreak.data.remote.streak.StreakResponse
import com.pedfu.daystreak.domain.notification.NotificationItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
class NotificationResponse(
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String,
    @Json(name = "message") val message: String?,
    @Json(name = "type") val type: String?,
    @Json(name = "read") val read: Boolean,
    @Json(name = "created_at") val createdAt: Date,
    @Json(name = "badge") val badge: BadgeResponse?,
    @Json(name = "streak") val streak: StreakResponse?,
) {
    fun toNotification(): NotificationItem = NotificationItem(
        id,
        title,
        message,
        type,
        read,
        createdAt,
        badge?.toBadge(),
        streak?.id,
    )
}