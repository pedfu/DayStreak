package com.pedfu.daystreak.domain.streak

import java.util.Date

enum class StreakStatus(s: String) {
    DAY_DONE("day_done"),
    PENDING("pending"),
    STREAK_OVER("streak_over");

    companion object {
        // Function to map a string to the corresponding enum value
        fun fromString(status: String?): StreakStatus? {
            if (status == null) return null
            return values().find { it.name.lowercase() == status }
        }
    }
}

data class StreakItem(
    val id: Long?,
    val name: String,
    val durationDays: Int?,
    val description: String?,
    val createdBy: String?,
    val categoryId: Long,
    val userStreakId: Long,
    val minTimePerDayInMinutes: Int,
    val status: StreakStatus = StreakStatus.PENDING,
    val backgroundPicture: String?,
    val createdAt: Date,
    val goalDeadLine: Date?,
    val maxStreak: Int = 0,
    val localBackgroundPicture: String? = null,
    val localBackgroundPictureRes: Int? = null,
) {
    constructor(item: StreakItem, localBackgroundPictureRes: Int?) : this(
        item.id,
        item.name,
        item.durationDays,
        item.description,
        item.createdBy,
        item.categoryId,
        item.userStreakId,
        item.minTimePerDayInMinutes,
        item.status,
        item.backgroundPicture,
        item.createdAt,
        item.goalDeadLine,
        item.maxStreak,
        item.localBackgroundPicture,
        localBackgroundPictureRes
    )
}