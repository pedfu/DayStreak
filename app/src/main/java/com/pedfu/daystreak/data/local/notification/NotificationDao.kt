package com.pedfu.daystreak.data.local.notification

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pedfu.daystreak.data.local.category.CategoryEntity
import com.pedfu.daystreak.domain.notification.NotificationItem
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
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

    @Query("UPDATE notification SET read = 1")
    suspend fun markAllAsRead()

    @Query("UPDATE notification SET read = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Transaction
    suspend fun refreshNotification(notifications: List<NotificationItem>) {
        val allNotifications = getAll()

        // Remove non existing (or removed) categories
        val notificationsToRemove = allNotifications.filter { notifications.any { c -> c.id != it.id } }
        notificationsToRemove.forEach { delete(it.id) }

        // Update existing notifications
        notifications.forEach { insert(NotificationEntity(it)) }
    }

    @Transaction
    @Query("DELETE FROM notification")
    suspend fun deleteAll()

    @Transaction
    @Query("DELETE FROM notification WHERE id = :id")
    suspend fun delete(id: Long)
}