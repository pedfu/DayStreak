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
        notificationService.getNotifications().forEach {
            // save notification
            saveNotification(it.toNotification())
        }
    }

    suspend fun saveNotification(notification: NotificationItem) {
        notificationRepository.saveNotification(notification)
    }
}