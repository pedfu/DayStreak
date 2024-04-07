package com.pedfu.daystreak.data.remote.streak

import com.pedfu.daystreak.data.remote.user.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface StreakApi {
    @GET("api/v1/streak/{id}")
    suspend fun getStreak(@Path("id") id: Long): StreakResponse?

    @GET("api/v1/streaks")
    suspend fun getStreaks(): Response<List<StreakResponse>>

    @POST("api/v1/streak")
    suspend fun createStreak(@Body streakRequest: StreakRequest): StreakResponse
}