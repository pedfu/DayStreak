package com.pedfu.daystreak.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.authorization.AuthorizationManager
import com.pedfu.daystreak.data.repositories.notification.NotificationRepository
import com.pedfu.daystreak.data.repositories.user.UserRepository
import com.pedfu.daystreak.usecases.refresh.RefreshUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

enum class MainState {
    IDLE,
    REFRESHING,
    REFRESHING_SWIPE
}
class MainViewModel(
    private val refreshUseCase: RefreshUseCase = Inject.refreshUseCase,
    private val authorizationManager: AuthorizationManager = Inject.authorizationManager,
    private val userRepository: UserRepository = Inject.userRepository,
    private val notificationRepository: NotificationRepository = Inject.notificationRepository,
) : ViewModel() {
    private var state: MainState = MainState.IDLE
    private var internalState: MainState = MainState.IDLE

    private val internalStateFlow = MutableStateFlow(internalState)
    private val stateFlow = combine(internalStateFlow, refreshUseCase.isRefreshingFlow) { internalState, isRefresing ->
        mergeState(internalState, isRefresing).also { state = it }
    }
    val stateLiveData = stateFlow.asLiveData()
    val authorizationLiveData = MutableLiveData<String?>(null)
    val notificationsLiveData = notificationRepository.notificationsFlow.asLiveData()

    init {
        refresh()
        authorizationLiveData.value = authorizationManager.token
    }

    private fun mergeState(internalStateP: MainState, isRefreshing: Boolean): MainState {
        return when {
            internalStateP != MainState.IDLE -> internalStateP
            isRefreshing -> MainState.REFRESHING
            else -> MainState.IDLE
        }
    }

    fun refresh(swipe: Boolean = false) {
        if (internalState != MainState.IDLE) return

        viewModelScope.launch {
            internalState = when(swipe) {
                true -> MainState.REFRESHING_SWIPE
                else -> MainState.REFRESHING
            }

            refreshUseCase.refresh()

            internalState = MainState.IDLE
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userRepository.clear()
        }
        authorizationManager.notifyUnauthorized()
    }
}