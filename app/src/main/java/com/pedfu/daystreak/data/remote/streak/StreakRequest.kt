package com.pedfu.daystreak.data.remote.streak

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.File
import java.time.LocalDate
import java.util.Date
import kotlin.math.min

@JsonClass(generateAdapter = true)
class StreakRequest (
    @Json(name = "id") val id: Long?,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "duration_days") val durationDays: Long?, // duration days
    @Json(name = "end_date") val endDate: String?, // duration days
    @Json(name = "background") val background: File?,
    @Json(name = "category") val category: CategoryRequest?,
    @Json(name = "category_id") val categoryId: Long?,
    @Json(name = "min_time_per_day") val minTimePerDay: Int?, // minutes
) {
    constructor(
        name: String,
        description: String,
        durationDays: Long?,
        endDate: String?,
        background: File?,
        categoryId: Long?,
        minTimePerDay: Int?
    ): this(
        null,
        name,
        description,
        durationDays,
        endDate,
        background,
        null,
        categoryId,
        minTimePerDay
    )
}

@JsonClass(generateAdapter = true)
class SimplifiedStreakRequest (
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "duration_days") val durationDays: Long?, // duration days
    @Json(name = "end_date") val endDate: String?, // duration days
    @Json(name = "category_id") val categoryId: Long?,
    @Json(name = "min_time_per_day") val minTimePerDay: Int?, // minutes
) {
    constructor(request: StreakRequest): this(
        request.name,
        request.description,
        request.durationDays,
        request.endDate,
        request.categoryId,
        request.minTimePerDay,
    )
}