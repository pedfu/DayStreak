package com.pedfu.daystreak.data.repositories.user

import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.local.user.UserDao
import com.pedfu.daystreak.data.local.user.UserEntity
import com.pedfu.daystreak.domain.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(
    private val userDao: UserDao = Inject.userDao
) {
    val userFlow: Flow<User?> = userDao.observe().map { it?.toUser() }

    suspend fun getUser(): User? {
        return userDao.findById(1L)?.toUser()
    }

    suspend fun saveUser(user: User) {
        userDao.insert(UserEntity(user))
    }

    suspend fun clear() {
        userDao.deleteAll()
    }
}