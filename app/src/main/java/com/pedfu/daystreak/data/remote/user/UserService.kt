package com.pedfu.daystreak.data.remote.user

import com.pedfu.daystreak.Inject
import com.squareup.moshi.Moshi
import retrofit2.HttpException

class UserService(
    private val userApi: UserApi = Inject.userApi,
    private val moshi: Moshi = Inject.moshi
): RemoteService(moshi) {

    suspend fun fetchUser(): UserResponse {
        return try {
            userApi.fetchUser()
        } catch (e: HttpException) {
            throw e
        }
    }

    suspend fun sendNotificationToken(token: String) {
        return try {
            userApi.sendNotificationToken(NotificationTokenRequest(token))
        } catch (e: HttpException) {
            throw e
        }
    }
}