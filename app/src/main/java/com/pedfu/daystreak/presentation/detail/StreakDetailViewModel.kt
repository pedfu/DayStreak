package com.pedfu.daystreak.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.repositories.notification.NotificationRepository
import com.pedfu.daystreak.data.repositories.streak.StreakRepository
import com.pedfu.daystreak.data.repositories.user.UserRepository
import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.domain.user.User
import com.pedfu.daystreak.usecases.streak.StreakUseCase
import kotlinx.coroutines.launch

enum class StreakDetailState {
    IDLE,
    LOADING,
}

class StreakDetailViewModel(
    private val streakId: Long,
    private val streakRepository: StreakRepository = Inject.streakRepository,
    private val userRepository: UserRepository = Inject.userRepository,
    private val notificationRepository: NotificationRepository = Inject.notificationRepository,
): ViewModel() {
    private var state = StreakDetailState.IDLE
        set(value) {
            field = value
            stateLiveData.value = value
        }
    private var streak: StreakItem? = null
        set(value) {
            field = value
            streakLiveData.value = value
        }

    val stateLiveData = MutableLiveData(state)
    val streakLiveData = MutableLiveData<StreakItem?>()
    val userLiveData: LiveData<User?> = userRepository.userFlow.asLiveData()
    val notificationsLiveData = notificationRepository.notificationsFlow.asLiveData()

    init {
        refreshDetails()
    }

    fun refreshDetails() {
        if (state != StreakDetailState.IDLE) return

        viewModelScope.launch {
            state = StreakDetailState.LOADING
            streak = streakRepository.getStreak(streakId)
            state = StreakDetailState.IDLE
        }
    }
}