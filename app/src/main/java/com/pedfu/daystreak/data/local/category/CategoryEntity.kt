package com.pedfu.daystreak.data.local.category

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "category"
)
class CategoryEntity (
    @PrimaryKey val id: Long,
    val name: String
) {

}