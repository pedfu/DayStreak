package com.pedfu.daystreak.data.remote.streak

import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.domain.streak.StreakStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
class StreakResponse (
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "duration_days") val durationDays: Int,
    @Json(name = "description") val description: String,
    @Json(name = "created_by") val createdBy: String,
    @Json(name = "category") val category: CategoryResponse,
    @Json(name = "user_streak_id") val userStreakId: Long,
    @Json(name = "status") val status: String,
    @Json(name = "background_picture") val backgroundPicture: String,
//    @Json(name = "created_at") val createdAt: Date,
    @Json(name = "max_streak") val maxStreak: Int,
) {
    fun toStreak(): StreakItem = StreakItem(
        id = id,
        name = name,
        durationDays = durationDays,
        description = description,
        createdBy = createdBy,
        categoryId = category.id,
        userStreakId = userStreakId,
        status = StreakStatus.fromString(status) ?: StreakStatus.PENDING,
        backgroundPicture = backgroundPicture,
        createdAt = Date(),
        maxStreak = maxStreak,
    )
}