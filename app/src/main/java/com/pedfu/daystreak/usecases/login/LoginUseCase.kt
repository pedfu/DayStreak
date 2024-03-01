package com.pedfu.daystreak.usecases.login

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.user.UserRequest
import com.pedfu.daystreak.data.remote.user.UserResponse
import com.pedfu.daystreak.data.remote.user.UserService
import com.pedfu.daystreak.data.repositories.user.UserRepository
import com.pedfu.daystreak.domain.user.User

class LoginUseCase(
    private val userService: UserService = Inject.userService,
    private val userRepository: UserRepository = Inject.userRepository,
) {
    suspend fun saveUserAndToken(user: FirebaseUser?, token: String?) {
        if (user != null && token != null) {
            val email = user.email ?: throw IllegalArgumentException("You need email")
            val request = UserRequest(email, user.displayName, user.uid, user.photoUrl.toString())
            val user = userService.createOrUpdate(request)

            userRepository.saveUser(user.toUser(token))
        }
    }
}

private fun UserResponse.toUser(token: String): User = User(
    id,
    token,
    username,
    email,
    uid,
    Uri.parse(photoUrl)
)