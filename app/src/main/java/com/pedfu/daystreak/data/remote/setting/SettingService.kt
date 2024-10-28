package com.pedfu.daystreak.data.remote.setting

import com.pedfu.daystreak.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class SettingService(
    private val settingApi: SettingApi = Inject.settingApi
) {
    suspend fun updateProfilePicture(file: File): String? {
        return try {
            val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            val imagePart = MultipartBody.Part.createFormData("profile_picture", file.name, requestBody)
            val path = settingApi.updateProfilePicture(imagePart).profilePicturePath
            return path
        } catch (e: Throwable) {
            return null
        }
    }
}