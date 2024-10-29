package com.pedfu.daystreak.data.repositories.user

import android.net.Uri
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.local.user.UserDao
import com.pedfu.daystreak.data.local.user.UserEntity
import com.pedfu.daystreak.data.remote.setting.SettingService
import com.pedfu.daystreak.domain.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File

class UserRepository(
    private val userDao: UserDao = Inject.userDao,
    private val settingService: SettingService = Inject.settingService,
) {
    val userFlow: Flow<User?> = userDao.observe().map { it?.toUser() }

    suspend fun updateProfilePicture(file: File) {
        val path = settingService.updateProfilePicture(file)
        val user = getUser()
        if (user != null && path != null) {
            user.photoUrl = Uri.parse(path)
            saveUser(user)
        }
    }

    suspend fun getUser(): User? {
        return userDao.get()?.toUser()
    }

    suspend fun saveUser(user: User) {
        userDao.insert(UserEntity(user))
    }

    suspend fun clear() {
        userDao.deleteAll()
    }
}