package com.pedfu.daystreak

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.pedfu.daystreak.data.AppDatabase
import com.pedfu.daystreak.data.remote.authorization.AuthorizationManager
import com.pedfu.daystreak.data.remote.authorization.AuthorizationManagerImpl
import com.pedfu.daystreak.data.remote.login.LoginApi
import com.pedfu.daystreak.data.remote.login.LoginService
import com.pedfu.daystreak.data.remote.signup.SignupApi
import com.pedfu.daystreak.data.remote.signup.SignupService
import com.pedfu.daystreak.data.remote.streak.StreakApi
import com.pedfu.daystreak.data.remote.user.UserApi
import com.pedfu.daystreak.data.remote.user.UserService
import com.pedfu.daystreak.data.repositories.streak.StreakRepository
import com.pedfu.daystreak.data.repositories.user.UserRepository
import com.pedfu.daystreak.data.stores.session.SESSION_STORE
import com.pedfu.daystreak.data.stores.session.SessionStore
import com.pedfu.daystreak.data.stores.session.SessionStoreImpl
import com.pedfu.daystreak.helpers.RetrofitBuilder
import com.pedfu.daystreak.usecases.login.LoginUseCase
import com.pedfu.daystreak.usecases.user.UserUseCase
import retrofit2.Retrofit
import retrofit2.create
import java.lang.reflect.Constructor

object Inject {
    lateinit var appContext: Context
        private set
    lateinit var database: AppDatabase
        private set
    lateinit var unauthorizedRetrofit: Retrofit
        private set
    lateinit var authorizedRetrofit: Retrofit
        private set
    lateinit var sessionStore: SessionStore
        private set
    lateinit var authorizationManager: AuthorizationManager
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

    val streakApi by lazy { authorizedRetrofit.create<StreakApi>() }

    val signupApi by lazy { unauthorizedRetrofit.create<SignupApi>() }
    val signupService by lazy { SignupService() }

    val loginApi by lazy { unauthorizedRetrofit.create<LoginApi>() }
    val loginService by lazy { LoginService() }
    val loginUseCase by lazy { LoginUseCase() }

    fun init(application: Application) {
        appContext = application.applicationContext
        database = AppDatabase.getDatabase(appContext)

        sessionStore = store(SESSION_STORE, ::SessionStoreImpl)
        authorizationManager = AuthorizationManagerImpl()

        unauthorizedRetrofit = RetrofitBuilder(authorizationManager = null).build()
        authorizedRetrofit = RetrofitBuilder(authorizationManager).build()
    }

    private inline fun <T> store(name: String, constructor: (SharedPreferences) -> T): T {
        val prefs = appContext.getSharedPreferences(name, Context.MODE_PRIVATE)
        return constructor(prefs)
    }
}