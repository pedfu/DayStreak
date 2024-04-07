package com.pedfu.daystreak.data.remote.notification

import com.pedfu.daystreak.domain.badge.Badge
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
class BadgeResponse (
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "icon") val icon: String?,
    @Json(name = "rarity") val rarity: RarityResponse,
) {
    fun toBadge(): Badge = Badge(
        id,
        name,
        icon,
        rarity.id,
        rarity.rarityOrder,
        rarity.name,
    )
}

@JsonClass(generateAdapter = true)
class RarityResponse (
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "rarity_order") val rarityOrder: Int,
)