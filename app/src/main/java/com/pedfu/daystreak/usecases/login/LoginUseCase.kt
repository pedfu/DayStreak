package com.pedfu.daystreak.usecases.login

import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.authorization.AuthorizationManager
import com.pedfu.daystreak.data.remote.login.LoginRequest
import com.pedfu.daystreak.data.remote.login.LoginService
import com.pedfu.daystreak.data.repositories.user.UserRepository

class LoginUseCase(
    private val loginService: LoginService = Inject.loginService,
    private val authorizationManager: AuthorizationManager = Inject.authorizationManager,
    private val userRepository: UserRepository = Inject.userRepository,
) {
    suspend fun login(usernameOrEmail: String, password: String): Boolean {
        val request = LoginRequest(usernameOrEmail, password)
        val response = loginService.login(request)
        if (response != null) {
            authorizationManager.token = response.tokenKey
            val user = response.user.toUser()
            userRepository.saveUser(user)
            return true
        }
        return false
    }
}
