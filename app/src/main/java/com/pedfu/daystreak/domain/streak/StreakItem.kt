package com.pedfu.daystreak.domain.streak

import java.util.Date

enum class StreakStatus(s: String) {
    DAY_DONE("day_done"),
    PENDING("pending"),
    STREAK_OVER("streak_over");

    companion object {
        // Function to map a string to the corresponding enum value
        fun fromString(status: String): StreakStatus? {
            return values().find { it.name.lowercase() == status }
        }
    }
}

data class StreakItem(
    val id: Long?,
    val name: String,
    val durationDays: Int?,
    val description: String?,
    val createdBy: String,
    val categoryId: Long,
    val userStreakId: Long,
    val status: StreakStatus = StreakStatus.PENDING,
    val backgroundPicture: String,
    val createdAt: Date,
    val maxStreak: Int = 0,
)