package com.pedfu.daystreak.data.remote.user

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("api/v1/user")
    suspend fun getUserByEmail(@Query("email") email: String): UserResponse

    @POST("api/v1/create-user")
    suspend fun createOrUpdateUser(@Body request: UserRequest): UserResponse

    @POST("api/v1/user/{id}")
    suspend fun updateUser(@Path("id") userId: Long, request: UserRequest): UserResponse
}