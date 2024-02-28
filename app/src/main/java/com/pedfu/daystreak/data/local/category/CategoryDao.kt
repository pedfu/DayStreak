package com.pedfu.daystreak.data.local.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): CategoryEntity?

    @Query("SELECT * FROM category WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<CategoryEntity?>

    @Query("SELECT * FROM category")
    fun observe(): Flow<List<CategoryEntity?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(streak: CategoryEntity)

    @Transaction
    @Query("DELETE FROM category")
    suspend fun deleteAll()
}