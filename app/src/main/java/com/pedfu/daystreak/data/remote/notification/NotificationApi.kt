package com.pedfu.daystreak.data.remote.notification

import retrofit2.Response
import retrofit2.http.GET

interface NotificationApi {
    @GET("api/v1/notifications/")
    suspend fun getNotifications(): Response<List<NotificationResponse>>
}