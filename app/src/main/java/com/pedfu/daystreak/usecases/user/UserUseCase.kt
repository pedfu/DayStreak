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
) {
    suspend fun getUser(): User? {
        return userRepository.getUser()
    }
}