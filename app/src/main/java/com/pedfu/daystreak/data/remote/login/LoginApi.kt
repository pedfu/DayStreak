package com.pedfu.daystreak.data.remote.login

import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("api/v1/login/")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}