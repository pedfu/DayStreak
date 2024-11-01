package com.pedfu.daystreak.data.local.streak

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pedfu.daystreak.data.local.category.CategoryEntity
import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.domain.streak.StreakStatus
import java.util.Date

@Entity(
    tableName = "streak",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index(value = ["categoryId"])
    ]
)
class StreakEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val name: String,
    val durationDays: Int?,
    val description: String?,
    val createdBy: String?,
    val categoryId: Long,
    val userStreakId: Long,
    val minTimePerDayInMinutes: Int,
    val status: String,
    val backgroundPicture: String?,
    val createdAt: Date,
    val goalDeadLine: Date?,
    val maxStreak: Int,
    val localBackgroundPicture: String?,
) {
    constructor(streak: StreakItem, id: Long? = null): this(
        id = id ?: streak.id,
        name = streak.name,
        durationDays = streak.durationDays,
        description = streak.description,
        createdBy = streak.createdBy,
        categoryId = streak.categoryId,
        userStreakId = streak.userStreakId,
        minTimePerDayInMinutes = streak.minTimePerDayInMinutes,
        status = streak.status.name,
        backgroundPicture = streak.backgroundPicture,
        createdAt = streak.createdAt,
        goalDeadLine = streak.goalDeadLine,
        maxStreak = streak.maxStreak,
        localBackgroundPicture = streak.localBackgroundPicture,
    )

    fun toStreak(): StreakItem = StreakItem(
        id = id,
        name = name,
        durationDays = durationDays,
        description = description,
        createdBy = createdBy,
        categoryId = categoryId,
        userStreakId = userStreakId,
        minTimePerDayInMinutes = minTimePerDayInMinutes,
        status = StreakStatus.fromString(status) ?: StreakStatus.PENDING,
        backgroundPicture = backgroundPicture,
        createdAt = createdAt,
        goalDeadLine = goalDeadLine,
        maxStreak = maxStreak,
        localBackgroundPicture = localBackgroundPicture,
    )
}