package com.pedfu.daystreak

import android.app.Application
import android.content.Context
import com.pedfu.daystreak.data.AppDatabase
import com.pedfu.daystreak.data.repositories.streak.StreakRepository
import com.pedfu.daystreak.data.repositories.user.UserRepository
import kotlin.coroutines.coroutineContext

object Inject {
    lateinit var appContext: Context
        private set

    lateinit var database: AppDatabase
        private set

    val userDao by lazy { database.userDao() }

    val userRepository by lazy { UserRepository() }

    val streakDao by lazy { database.streakDao() }

    val streakRepository by lazy { StreakRepository() }

    val categoryDao by lazy { database.categoryDao() }

//    val categoryRepository by lazy { CategoryRepository() }

    fun init(application: Application) {
        appContext = application.applicationContext
        database = AppDatabase.getDatabase(appContext)
    }
}