package com.pedfu.daystreak.usecases.user

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.user.UserRequest
import com.pedfu.daystreak.data.remote.user.UserResponse
import com.pedfu.daystreak.data.remote.user.UserService
import com.pedfu.daystreak.data.repositories.user.UserRepository
import com.pedfu.daystreak.domain.user.User

class UserUseCase(
    private val userRepository: UserRepository = Inject.userRepository,
    private val userService: UserService = Inject.userService,
) {
    suspend fun getUser(): User? {
        return userRepository.getUser()
    }

    suspend fun fetchUser() {
        val user = userService.fetchUser()
        val uri = if (user.photoUrl != null) Uri.parse(user.photoUrl) else null
        userRepository.saveUser(user.toUser())
    }
}