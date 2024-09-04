package com.pedfu.daystreak.data.remote.setting

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface SettingApi {
    @Multipart
    @PUT("api/v1/user/profile-picture/")
    suspend fun updateProfilePicture(@Part image: MultipartBody.Part)
}