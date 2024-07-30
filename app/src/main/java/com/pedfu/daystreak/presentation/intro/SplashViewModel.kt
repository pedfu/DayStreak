package com.pedfu.daystreak.presentation.intro

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.domain.user.User
import com.pedfu.daystreak.usecases.streak.StreakUseCase
import com.pedfu.daystreak.usecases.user.UserUseCase
import kotlinx.coroutines.launch
import kotlin.math.log

enum class SplashState {
    LOADING,
    LOGGED_IN,
    NOT_LOGGED_IN,
}
class SplashViewModel(
    private val userUseCase: UserUseCase = Inject.userUseCase,
    private val streakUseCase: StreakUseCase = Inject.streakUseCase,
) : ViewModel() {
    private var state: SplashState = SplashState.LOADING
        set(value) {
            field = value
            stateLiveData.value = value
        }
    private var loggedInUser: User? = null
        set(value) {
            field = value
            loggedInUserLiveData.value = value
        }
    val loggedInUserLiveData = MutableLiveData(loggedInUser)
    val stateLiveData = MutableLiveData(state)

    init {
        viewModelScope.launch {
            state = SplashState.LOADING
            loggedInUser = userUseCase.getUser()
            if (loggedInUser != null) {
                streakUseCase.fetchStreaks()
            }
            state = when (loggedInUser) {
                null -> SplashState.NOT_LOGGED_IN
                else -> SplashState.LOGGED_IN
            }
        }
    }
}