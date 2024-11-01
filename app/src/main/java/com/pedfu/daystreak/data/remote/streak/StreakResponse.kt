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
    @Json(name = "duration_days") val durationDays: Int?,
    @Json(name = "goal_deadline") val goalDeadLine: String?,
    @Json(name = "description") val description: String,
    @Json(name = "created_by") val createdBy: String?,
    @Json(name = "category") val category: CategoryResponse,
    @Json(name = "user_streak_id") val userStreakId: Long,
    @Json(name = "min_time_per_day") val minTimePerDayInMinutes: Int,
    @Json(name = "status") val status: String?,
    @Json(name = "background_picture") val backgroundPicture: String?,
    @Json(name = "max_streak") val maxStreak: Int?,
    @Json(name = "local_background_picture") val localBackgroundPicture: String?,
) {
    fun toStreak(): StreakItem = StreakItem(
        id = id,
        name = name,
        durationDays = durationDays,
        description = description,
        createdBy = createdBy,
        categoryId = category.id,
        userStreakId = userStreakId,
        minTimePerDayInMinutes = minTimePerDayInMinutes,
        status = StreakStatus.fromString(status) ?: StreakStatus.PENDING,
        backgroundPicture = backgroundPicture,
        createdAt = Date(),
        goalDeadLine = goalDeadLine,
        maxStreak = maxStreak ?: 0,
        localBackgroundPicture = localBackgroundPicture,
    )
}