package com.pedfu.daystreak.data.remote.notification

import com.pedfu.daystreak.Inject
import retrofit2.HttpException

class NotificationService(
    private val notificationApi: NotificationApi = Inject.nofiticationApi
) {
    suspend fun getNotifications(): List<NotificationResponse> {
        return try {
            val response = notificationApi.getNotifications()
            response.body() ?: emptyList()
        } catch (e: Throwable) {
            throw e
        }
    }

    suspend fun markAsRead(id: Long): Boolean {
        return try {
            notificationApi.markAsRead(id)
            true
        } catch (e: Throwable) {
            false
        }
    }

    suspend fun markAllAsRead(): Boolean {
        return try {
            notificationApi.markAllAsRead()
            true
        } catch (e: Throwable) {
            false
        }
    }

    suspend fun clearAll(): Boolean {
        return try {
            notificationApi.clearAll()
            true
        } catch (e: Throwable) {
            false
        }
    }
}