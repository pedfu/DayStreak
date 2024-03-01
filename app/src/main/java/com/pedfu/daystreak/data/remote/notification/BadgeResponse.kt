package com.pedfu.daystreak.data.remote.notification

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
class BadgeResponse (
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "picture") val picture: String,
    @Json(name = "rarity") val rarity: RarityResponse,
)

@JsonClass(generateAdapter = true)
class RarityResponse (
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "rarity_order") val rarityOrder: Long,
)