package com.pedfu.daystreak.data.remote.streak

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
class CompleteDayRequest (
    @Json(name = "id") val id: Long,
    @Json(name = "date") val date: Date?,
    @Json(name = "seconds") val seconds: Int,
    @Json(name = "description") val description: String?,
) {
}