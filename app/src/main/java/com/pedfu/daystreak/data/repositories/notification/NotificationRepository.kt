package com.pedfu.daystreak.data.repositories.notification

import android.util.Log
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.local.notification.NotificationDao
import com.pedfu.daystreak.data.local.notification.NotificationEntity
import com.pedfu.daystreak.domain.notification.NotificationItem
import com.pedfu.daystreak.domain.streak.StreakItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotificationRepository(
    private val notificationDao: NotificationDao = Inject.notificationDao
) {
    val notificationsFlow: Flow<List<NotificationItem>> = notificationDao.observe().map {
        it.map { s -> s.toNotification() }
    }

    suspend fun saveNotification(notification: NotificationItem) {
        notificationDao.insert(NotificationEntity(notification))
    }

    suspend fun onRefreshNotifications(notifications: List<NotificationItem>) {
        notificationDao.refreshNotification(notifications)
    }

    suspend fun markAllAsRead() {
        notificationDao.markAllAsRead()
        // call API
    }

    suspend fun markAsRead(id: Long) {
        notificationDao.markAsRead(id)
        // call API
    }

    suspend fun removeAll() {
        notificationDao.deleteAll()
        // call API
    }
}