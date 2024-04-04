package com.pedfu.daystreak.presentation.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.domain.user.User
import com.pedfu.daystreak.usecases.login.LoginUseCase
import com.pedfu.daystreak.usecases.user.UserUseCase
import kotlinx.coroutines.launch

enum class SignInState {
    IDLE,
    LOADING,
    LOGGED_IN,
    ERROR
}
class SignInViewModel(
    private val loginUseCase: LoginUseCase = Inject.loginUseCase,
    private val userUseCase: UserUseCase = Inject.userUseCase,
): ViewModel() {
    private var state: SignInState = SignInState.IDLE
        set(value) {
            field = value
            stateLiveData.value = value
        }
    private var user: User? = null

    val stateLiveData = MutableLiveData<SignInState>()

    init {
        viewModelScope.launch {
            user = userUseCase.getUser()
            if (user != null) state = SignInState.LOGGED_IN
        }
    }

    fun saveUser(fUser: FirebaseUser?, token: String?) {
        viewModelScope.launch {
            state = SignInState.LOADING
//            loginUseCase.saveUserAndToken(fUser, token)
            state = SignInState.LOGGED_IN
        }
    }
}