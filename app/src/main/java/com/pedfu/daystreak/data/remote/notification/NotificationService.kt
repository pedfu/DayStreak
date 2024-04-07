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
        } catch (e: HttpException) {
            throw e
        }
    }
}