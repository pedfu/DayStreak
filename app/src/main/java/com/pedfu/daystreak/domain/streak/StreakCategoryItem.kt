package com.pedfu.daystreak.domain.streak

data class StreakCategoryItem(
    val id: Long,
    val name: String,
    var selected: Boolean = false,
)