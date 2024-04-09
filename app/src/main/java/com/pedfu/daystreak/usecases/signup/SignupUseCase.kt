package com.pedfu.daystreak.usecases.signup

import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.signup.SignupRequest
import com.pedfu.daystreak.data.remote.signup.SignupService

class SignupUseCase(
    private val signupService: SignupService = Inject.signupService,
) {
    suspend fun signup(email: String, username: String, firstName: String, lastName: String, password: String) {
        val request = SignupRequest(email, username, firstName, lastName, password)
        signupService.signup(request)
    }
}
