package com.pedfu.daystreak.domain.streak

import java.util.Date

enum class StreakStatus {
    DAY_DONE,
    PENDING,
    STREAK_OVER,
}

data class StreakItem(
    val id: Long?,
    val name: String,
    val durationDays: Int,
    val description: String?,
    val createdBy: String,
    val categoryId: Long,
    val userStreakId: Long,
    val status: StreakStatus = StreakStatus.PENDING,
    val backgroundPicture: String,
    val createdAt: Date,
    val maxStreak: Int = 0,
)