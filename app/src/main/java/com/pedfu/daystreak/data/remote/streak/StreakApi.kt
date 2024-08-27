package com.pedfu.daystreak.data.remote.streak

import com.pedfu.daystreak.data.remote.user.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StreakApi {
    @GET("api/v1/streak/{id}")
    suspend fun getStreak(@Path("id") id: Long): StreakResponse?

    @GET("api/v1/streaks/")
    suspend fun getStreaks(): Response<List<StreakResponse>>

    @GET("api/v1/category/")
    suspend fun getCategories(): Response<List<CategoryResponse>>

    @Multipart
    @POST("api/v1/streaks/")
    suspend fun createStreak(
        @Part(value = "data") streakRequest: SimplifiedStreakRequest,
        @Part backgroundPart: MultipartBody.Part?
    ): StreakResponse?

    @POST("api/v1/category/")
    suspend fun createCategory(@Body streakRequest: CategoryRequest): CategoryResponse

    @POST("api/v1/category/{id}/delete/")
    suspend fun deleteCategory(@Path("id") id: Long): Boolean

    @POST("api/v1/streak/{id}/delete/")
    suspend fun deleteStreak(@Path("id") id: Long): Boolean
}