package com.pedfu.daystreak.data.repositories.notification

import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.local.notification.NotificationDao
import com.pedfu.daystreak.data.local.notification.NotificationEntity
import com.pedfu.daystreak.domain.notification.NotificationItem
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
}