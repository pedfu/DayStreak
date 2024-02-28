package com.pedfu.daystreak.data.local.streak

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface StreakDao {
    @Query("SELECT * FROM streak WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): StreakEntity?

    @Query("SELECT * FROM streak WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<StreakEntity?>

    @Query("SELECT * FROM streak")
    fun observe(): Flow<List<StreakEntity?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(streak: StreakEntity)

    @Transaction
    @Query("DELETE FROM streak")
    suspend fun deleteAll()
}