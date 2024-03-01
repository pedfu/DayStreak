package com.pedfu.daystreak.data.remote.streak

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
class StreakTrackResponse (
    @Json(name = "id") val id: Long, // in milli? sec? min?
    @Json(name = "duration") val name: Double, // in milli? sec? min?
    @Json(name = "start_date") val startDate: Date,
    @Json(name = "end_date") val endDate: Date,
    @Json(name = "streak_id") val streakId: Long,
    @Json(name = "description") val description: String?
)