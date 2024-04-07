package com.pedfu.daystreak.domain.badge

data class Badge(
    val id: Long,
    val name: String,
    val icon: String?,
    val rarityId: Long,
    val rarityOrder: Int,
    val rarityName: String,
)
