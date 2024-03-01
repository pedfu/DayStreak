package com.pedfu.daystreak.data.remote.user

import com.pedfu.daystreak.Inject
import retrofit2.HttpException

class UserService(
    private val userApi: UserApi = Inject.userApi
): RemoteService() {

    suspend fun createOrUpdate(userRequest: UserRequest): UserResponse {
        return try {
            userApi.createOrUpdateUser(userRequest)
        } catch (e: HttpException) {
            throw e
        }
    }
}