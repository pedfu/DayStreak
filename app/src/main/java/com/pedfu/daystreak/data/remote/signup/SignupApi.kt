package com.pedfu.daystreak.data.remote.signup

import retrofit2.http.Body
import retrofit2.http.POST

interface SignupApi {
    @POST("api/v1/signup/")
    suspend fun signup(@Body request: SignupRequest)

}