package com.pedfu.daystreak.data.remote.user

import com.pedfu.daystreak.Inject
import com.squareup.moshi.Moshi
import retrofit2.HttpException

class UserService(
    private val userApi: UserApi = Inject.userApi,
    private val moshi: Moshi = Inject.moshi
): RemoteService(moshi) {

    suspend fun createOrUpdate(userRequest: UserRequest): UserResponse {
        return try {
            userApi.createOrUpdateUser(userRequest)
        } catch (e: HttpException) {
            throw e
        }
    }
}