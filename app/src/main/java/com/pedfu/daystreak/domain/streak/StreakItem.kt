package com.pedfu.daystreak.domain.streak

enum class StreakStatus {
    DAY_DONE,
    PENDING,
    STREAK_OVER,
}

data class StreakItem(
    val id: Long,
    val backgroundPicture: String,
    val title: String,
    val description: String?,
    val categoryId: Long,
    val status: StreakStatus = StreakStatus.PENDING,
)