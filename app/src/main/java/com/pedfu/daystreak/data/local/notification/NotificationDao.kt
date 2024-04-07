package com.pedfu.daystreak.data.local.notification

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notification")
    suspend fun getAll(): List<NotificationEntity>

    @Query("SELECT * FROM notification WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): NotificationEntity?

    @Query("SELECT * FROM notification WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<NotificationEntity?>

    @Query("SELECT * FROM notification")
    fun observe(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity)

    @Transaction
    @Query("DELETE FROM notification")
    suspend fun deleteAll()
}