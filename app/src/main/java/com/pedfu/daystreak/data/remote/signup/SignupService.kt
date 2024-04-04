package com.pedfu.daystreak.data.remote.signup

import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.login.LoginApi
import com.pedfu.daystreak.data.remote.login.LoginRequest
import com.pedfu.daystreak.data.remote.login.LoginResponse
import com.pedfu.daystreak.data.remote.user.RemoteService
import retrofit2.HttpException

class SignupService (
    private val signupApi: SignupApi = Inject.signupApi
): RemoteService() {
    suspend fun signup(signupRequest: SignupRequest): SignupResponse {
        return try {
            signupApi.signup(signupRequest)
        } catch (e: HttpException) {
            throw e
        }
    }
}