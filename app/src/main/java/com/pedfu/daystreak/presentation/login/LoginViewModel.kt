package com.pedfu.daystreak.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.repositories.user.UserRepository
import com.pedfu.daystreak.usecases.login.LoginUseCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

enum class LoginState {
    IDLE,
    LOADING,
    READY,
    LOGGED_IN,
    ERROR,
}

class LoginViewModel(
    private val loginUseCase: LoginUseCase = Inject.loginUseCase,
    private val userRepository: UserRepository = Inject.userRepository
) : ViewModel() {
    private var usernameOrEmail: String = ""
        set(value) {
            field = value
            usernameOrPasswordLiveData.value = value
        }
    private var password: String = ""
        set(value) {
            field = value
            passwordLiveData.value = value
        }
    private var state: LoginState = LoginState.IDLE
        set(value) {
            field = value
            stateLiveData.value = value
        }

    val stateLiveData = MutableLiveData(state)
    val usernameOrPasswordLiveData = MutableLiveData(usernameOrEmail)
    val passwordLiveData = MutableLiveData(password)

    fun onUsernameOrEmailChanged(text: String) {
        usernameOrEmail = text
        checkDataState()
    }
    fun onPasswordChanged(text: String) {
        password = text
        checkDataState()
    }

    private fun checkDataState() {
        val ready = !usernameOrEmail.isNullOrBlank() && !password.isNullOrBlank()
        state = when (ready) {
            true -> LoginState.READY
            else -> LoginState.IDLE
        }
    }

    fun onLoginClicked() {
        if (state < LoginState.READY) return
        viewModelScope.launch {
            state = LoginState.LOADING
            loginUseCase.login(usernameOrEmail, password)
            val user = userRepository.getUser()
            state = when (user) {
                null -> LoginState.ERROR
                else -> LoginState.LOGGED_IN
            }
        }
    }
}