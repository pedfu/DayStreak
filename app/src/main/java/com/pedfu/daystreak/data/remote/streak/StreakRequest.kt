package com.pedfu.daystreak.data.remote.streak

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.File

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
    @Json(name = "min_time_per_day") val minTimePerDay: Int, // minutes
    @Json(name = "local_background") val localBackground: String?,
    @Json(name = "goal_deadline") val goalDeadline: String?
) {
    constructor(
        name: String,
        description: String,
        durationDays: Long?,
        endDate: String?,
        background: File?,
        categoryId: Long?,
        minTimePerDay: Int,
        localBackground: String?,
        goalDeadline: String?,
    ): this(
        null,
        name,
        description,
        durationDays,
        endDate,
        background,
        null,
        categoryId,
        minTimePerDay,
        localBackground,
        goalDeadline
    )
}

@JsonClass(generateAdapter = true)
class SimplifiedStreakRequest (
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "duration_days") val durationDays: Long?, // duration days
    @Json(name = "end_date") val endDate: String?, // duration days
    @Json(name = "category_id") val categoryId: Long?,
    @Json(name = "min_time_per_day") val minTimePerDay: Int, // minutes
    @Json(name = "local_background_picture") val localBackground: String?, // minutes
    @Json(name = "goal_deadline") val goalDeadline: String?
) {
    constructor(request: StreakRequest): this(
        name = request.name,
        description = request.description,
        durationDays = request.durationDays,
        endDate = request.endDate,
        categoryId = request.categoryId,
        minTimePerDay = request.minTimePerDay,
        localBackground = request.localBackground,
        goalDeadline = request.goalDeadline,
    )
}