package com.pedfu.daystreak.data.remote.notification

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationApi {
    @GET("api/v1/notifications/")
    suspend fun getNotifications(): Response<List<NotificationResponse>>

    @POST("api/v1/notifications/clear/")
    suspend fun clearAll()

    @POST("api/v1/notifications/{id}/read/")
    suspend fun markAsRead(@Path("id") id: Long)

    @POST("api/v1/notifications/read/")
    suspend fun markAllAsRead()
}