package com.pedfu.daystreak.data.remote.signup

import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.login.LoginApi
import com.pedfu.daystreak.data.remote.login.LoginRequest
import com.pedfu.daystreak.data.remote.login.LoginResponse
import com.pedfu.daystreak.data.remote.user.RemoteService
import com.squareup.moshi.Moshi
import retrofit2.HttpException

class SignupService (
    private val signupApi: SignupApi = Inject.signupApi,
    private val moshi: Moshi = Inject.moshi
): RemoteService(moshi) {
    suspend fun signup(signupRequest: SignupRequest): Boolean {
        return try {
            signupApi.signup(signupRequest)
            return true
        } catch (e: HttpException) {
//            throw e
            return false
        }
    }
}