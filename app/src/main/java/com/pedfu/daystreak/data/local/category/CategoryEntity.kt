package com.pedfu.daystreak.data.local.category

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pedfu.daystreak.domain.streak.StreakCategoryItem

@Entity(
    tableName = "category"
)
class CategoryEntity (
    @PrimaryKey val id: Long,
    val name: String
) {

    fun toCategory(): StreakCategoryItem = StreakCategoryItem(
        id,
        name
    )
}