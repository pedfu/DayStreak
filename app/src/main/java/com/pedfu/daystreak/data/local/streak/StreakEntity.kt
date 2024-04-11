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
    val createdBy: String,
    val categoryId: Long,
    val userStreakId: Long,
    val status: String,
    val backgroundPicture: String,
    val createdAt: Date,
    val maxStreak: Int,
) {
    constructor(streak: StreakItem): this(
        id = streak.id,
        name = streak.name,
        durationDays = streak.durationDays,
        description = streak.description,
        createdBy = streak.createdBy,
        categoryId = streak.categoryId,
        userStreakId = streak.userStreakId,
        status = streak.status.name,
        backgroundPicture = streak.backgroundPicture,
        createdAt = streak.createdAt,
        maxStreak = streak.maxStreak,
    )

    fun toStreak(): StreakItem = StreakItem(
        id = id,
        name = name,
        durationDays = durationDays,
        description = description,
        createdBy = createdBy,
        categoryId = categoryId,
        userStreakId = userStreakId,
        status = StreakStatus.fromString(status) ?: StreakStatus.PENDING,
        backgroundPicture = backgroundPicture,
        createdAt = createdAt,
        maxStreak = maxStreak,
    )
}