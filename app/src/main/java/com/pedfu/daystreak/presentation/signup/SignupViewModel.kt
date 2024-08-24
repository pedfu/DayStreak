package com.pedfu.daystreak.presentation.signup

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.repositories.user.UserRepository
import com.pedfu.daystreak.usecases.signup.SignupUseCase
import kotlinx.coroutines.launch


enum class SignupState {
    IDLE,
    LOADING,
    READY,
    DATA_SENT,
    ERROR,
}

class SignupViewModel(
    private val signupUseCase: SignupUseCase = Inject.signupUseCase,
    private val userRepository: UserRepository = Inject.userRepository
) : ViewModel() {
    private var email: String = ""
        set(value) {
            field = value
            emailLiveData.value = value
        }
    private var firstName: String = ""
        set(value) {
            field = value
            firstNameLiveData.value = value
        }
    private var lastName: String = ""
        set(value) {
            field = value
            lastNameLiveData.value = value
        }
    private var username: String = ""
        set(value) {
            field = value
            usernameLiveData.value = value
        }
    private var password: String = ""
        set(value) {
            field = value
            passwordLiveData.value = value
        }
    private var state: SignupState = SignupState.IDLE
        set(value) {
            field = value
            stateLiveData.value = value
        }

    val stateLiveData = MutableLiveData(state)
    val emailLiveData = MutableLiveData(email)
    val firstNameLiveData = MutableLiveData(firstName)
    val lastNameLiveData = MutableLiveData(lastName)
    val usernameLiveData = MutableLiveData(username)
    val passwordLiveData = MutableLiveData(password)
    val emailErrorLiveData = MutableLiveData<Boolean>(false)
    val passwordErrorLiveData = MutableLiveData<Boolean>(false)

    fun onEmailChanged(text: String) {
        email = text
        checkDataState()
    }
    fun onFirstNameChanged(text: String) {
        firstName = text
        checkDataState()
    }
    fun onLastNameChanged(text: String) {
        lastName = text
        checkDataState()
    }
    fun onUsernameChanged(text: String) {
        username = text
        checkDataState()
    }
    fun onPasswordChanged(text: String) {
        password = text
        checkDataState()
    }

    private fun checkDataState() {
        if (isValidEmail(email)) emailErrorLiveData.value = false

        val passwordValid = isSafePassword()
        if (passwordValid) passwordErrorLiveData.value = false
        val ready =
            !email.isNullOrBlank() &&
            !firstName.isNullOrBlank() &&
            !lastName.isNullOrBlank() &&
            !username.isNullOrBlank() &&
            !password.isNullOrBlank()
        state = when (ready) {
            true -> SignupState.READY
            else -> SignupState.IDLE
        }
    }

    fun onSignupClicked() {
        if (!isValidEmail(email)) {
            emailErrorLiveData.value = true
            return
        }
        if (!isSafePassword()) {
            passwordErrorLiveData.value = true
            return
        }
        if (state < SignupState.READY) return
        viewModelScope.launch {
            state = SignupState.LOADING
            signupUseCase.signup(email, username, firstName, lastName, password)
            state = SignupState.DATA_SENT
        }
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
    private fun isSafePassword(): Boolean {
        val hasUppercase = password != password.lowercase()
        val regex = "[^a-zA-Z0-9]".toRegex()
        val hasSpecialChars = regex.containsMatchIn(password)
        return password.length > 11 && hasUppercase && hasSpecialChars
    }
}