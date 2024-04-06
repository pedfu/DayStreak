package com.pedfu.daystreak.data.remote.login

import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.user.RemoteService
import com.squareup.moshi.Moshi
import retrofit2.HttpException

class LoginService (
    private val loginApi: LoginApi = Inject.loginApi,
    moshi: Moshi = Inject.moshi
): RemoteService(moshi) {
    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return wrapError {
            loginApi.login(loginRequest)
        }
    }
}