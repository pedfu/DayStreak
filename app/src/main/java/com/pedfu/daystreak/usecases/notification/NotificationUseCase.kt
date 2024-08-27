package com.pedfu.daystreak.usecases.notification

import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.notification.NotificationService
import com.pedfu.daystreak.data.repositories.notification.NotificationRepository
import com.pedfu.daystreak.domain.notification.NotificationItem

class NotificationUseCase(
    private val notificationService: NotificationService = Inject.notificationService,
    private val notificationRepository: NotificationRepository = Inject.notificationRepository,
) {
    suspend fun fetchNotifications() {
        val notifications = notificationService.getNotifications()
        notificationRepository.onRefreshNotifications(notifications.map { it.toNotification() })
    }

    suspend fun markNotificationAsRead(id: Long) {
        val result = notificationService.markAsRead(id)
        if (result) notificationRepository.markAsRead(id)
    }

    suspend fun markAllNotificationsAsRead() {
        val result = notificationService.markAllAsRead()
        if (result) notificationRepository.markAllAsRead()
    }

    suspend fun clearAllNotifications() {
        val result = notificationService.clearAll()
        if (result) notificationRepository.clearAll()
    }
}