package com.pedfu.daystreak.data.local.badge

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pedfu.daystreak.domain.badge.Badge

@Entity(
    tableName = "badge",
)
class BadgeEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val icon: String?,
    val rarityId: Long,
    val rarityOrder: Int,
    val rarityName: String,
) {
    constructor(badge: Badge): this(
        id = badge.id,
        name = badge.name,
        icon = badge.icon,
        rarityId = badge.rarityId,
        rarityOrder = badge.rarityOrder,
        rarityName = badge.rarityName,
    )

    fun toBadge(): Badge = Badge(
        id,
        name,
        icon,
        rarityId,
        rarityOrder,
        rarityName,
    )
}