package com.pedfu.daystreak.data.local.streak

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pedfu.daystreak.data.local.category.CategoryEntity
import com.pedfu.daystreak.domain.streak.StreakItem

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
    val description: String?,
    val categoryId: Long, // modificar para relacionamento
    val status: String,
    val backgroundUri: String,
    val currentStreakCount: Int,
) {
    constructor(streak: StreakItem): this(
        id = streak.id,
        name = streak.name,
        description = streak.description,
        categoryId = streak.categoryId,
        status = streak.status.name,
        backgroundUri = streak.backgroundPicture,
        currentStreakCount = streak.currentStreakCount,
    )
}