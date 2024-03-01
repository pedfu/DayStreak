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
    private val userService: UserService = Inject.userService,
    private val userRepository: UserRepository = Inject.userRepository,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    suspend fun createOrUpdateUser(user: User) {
        if (auth.currentUser != null && user.email != null) {
            val request = UserRequest(user.email, user.username, user.uid, user.photoUrl.toString())
//            val response = userService.createOrUpdate(request)
            val response = UserResponse(
                1,
                "arisareis1@gmail.com",
                "pedfu",
                "teste",
                "https://lh3.googleusercontent.com/a/ACg8ocLpIcU5pnqjjDLovy1sIJPO-9PZbSnh3FRZ3EW9WgG4mA=s96-c",
            )
            userRepository.saveUser(response.toUser(user.token))
        }
    }

    suspend fun getUser(): User? {
        return userRepository.getUser()
    }
}

fun UserResponse.toUser(token: String): User = User(
    id,
    token,
    username,
    email,
    uid,
    if (photoUrl != null) Uri.parse(photoUrl) else null,
)