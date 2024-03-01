package com.pedfu.daystreak.data.remote.streak

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
class StreakRequest (
    @Json(name = "id") val id: Long?,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "streak_goal") val streakGoal: Int?, // duration days
    @Json(name = "created_at") val createdAt: Date, // for offline update
    @Json(name = "background") val background: String,
    @Json(name = "category") val category: CategoryRequest
)