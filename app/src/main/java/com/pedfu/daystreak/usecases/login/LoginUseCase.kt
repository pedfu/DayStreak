package com.pedfu.daystreak.usecases.login

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.authorization.AuthorizationManager
import com.pedfu.daystreak.data.remote.login.LoginRequest
import com.pedfu.daystreak.data.remote.login.LoginService
import com.pedfu.daystreak.data.remote.signup.SignupRequest
import com.pedfu.daystreak.data.remote.user.UserRequest
import com.pedfu.daystreak.data.remote.user.UserResponse
import com.pedfu.daystreak.data.remote.user.UserService
import com.pedfu.daystreak.data.repositories.user.UserRepository
import com.pedfu.daystreak.domain.user.User

class LoginUseCase(
    private val loginService: LoginService = Inject.loginService,
    private val authorizationManager: AuthorizationManager = Inject.authorizationManager,
    private val userRepository: UserRepository = Inject.userRepository,
) {
    suspend fun login(usernameOrEmail: String, password: String) {
        val request = LoginRequest(usernameOrEmail, password)
        val response = loginService.login(request)
        authorizationManager.token = response.tokenKey
        val user = response.user.toUser()
        userRepository.saveUser(user)
    }
}
