package com.pedfu.daystreak

import android.app.Application
import android.content.Context
import com.pedfu.daystreak.data.AppDatabase
import com.pedfu.daystreak.data.remote.streak.StreakApi
import com.pedfu.daystreak.data.remote.user.UserApi
import com.pedfu.daystreak.data.remote.user.UserService
import com.pedfu.daystreak.data.repositories.streak.StreakRepository
import com.pedfu.daystreak.data.repositories.user.UserRepository
import com.pedfu.daystreak.helpers.RetrofitBuilder
import com.pedfu.daystreak.usecases.user.UserUseCase
import retrofit2.Retrofit
import retrofit2.create

object Inject {
    lateinit var appContext: Context
        private set

    lateinit var database: AppDatabase
        private set

    lateinit var unauthorizedRetrofit: Retrofit
        private set

    val userDao by lazy { database.userDao() }
    val userRepository by lazy { UserRepository() }

    val streakDao by lazy { database.streakDao() }
    val streakRepository by lazy { StreakRepository() }

    val categoryDao by lazy { database.categoryDao() }
//    val categoryRepository by lazy { CategoryRepository() }

    // APIs
    val userApi by lazy { unauthorizedRetrofit.create<UserApi>() }
    val userService by lazy { UserService() }
    val userUseCase by lazy { UserUseCase() }

    val streakApi by lazy { unauthorizedRetrofit.create<StreakApi>() }

    fun init(application: Application) {
        appContext = application.applicationContext
        database = AppDatabase.getDatabase(appContext)
        unauthorizedRetrofit = RetrofitBuilder().build()
    }
}