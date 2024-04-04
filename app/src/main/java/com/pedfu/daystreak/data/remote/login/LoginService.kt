package com.pedfu.daystreak.data.remote.login

import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.user.RemoteService
import retrofit2.HttpException

class LoginService (
    private val loginApi: LoginApi = Inject.loginApi
): RemoteService() {
    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return try {
            loginApi.login(loginRequest)
        } catch (e: HttpException) {
            throw e
        }
    }
}