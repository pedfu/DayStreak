package com.pedfu.daystreak.presentation.header.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.repositories.notification.NotificationRepository
import com.pedfu.daystreak.domain.notification.NotificationItem
import com.pedfu.daystreak.usecases.notification.NotificationUseCase
import kotlinx.coroutines.launch

enum class NotificationState {
    IDLE,
    LOADING,
    MARKING_READ,
    CLEARING,
    ACCEPT_CHALLENGE,
    DECLINE_CHALLENGE,
}

class NotificationViewModel(
    private val notificationUseCase: NotificationUseCase = Inject.notificationUseCase,
    private val notificationRepository: NotificationRepository = Inject.notificationRepository,
) : ViewModel() {

    private var state: NotificationState = NotificationState.IDLE
        set(value) {
            field = value
        }

    val notificationsLiveData = notificationRepository.notificationsFlow.asLiveData()
    val stateLiveData = MutableLiveData(state)

    fun markAllAsRead() {
        if (notificationsLiveData.value?.none { !it.read } == true) return
        viewModelScope.launch {
            state = NotificationState.MARKING_READ
            notificationUseCase.markAllNotificationsAsRead()
            state = NotificationState.IDLE
        }
    }

    fun cleanAll() {
        if (notificationsLiveData.value?.isEmpty() == true) return
        viewModelScope.launch {
            state = NotificationState.CLEARING
            notificationUseCase.clearAllNotifications()
            state = NotificationState.IDLE
        }
    }

    fun markItemRead(id: Long) {
        val notification = notificationsLiveData.value?.find { it.id == id }
        if (notification == null || (notification != null && notification.read)) return

        viewModelScope.launch {
            state = NotificationState.MARKING_READ
            notificationUseCase.markNotificationAsRead(id)
            state = NotificationState.IDLE
        }
    }

    fun onAcceptChallenge(id: Long) {
        viewModelScope.launch {
            state = NotificationState.ACCEPT_CHALLENGE
            // create use case to accept
            state = NotificationState.IDLE
        }
    }

    fun onDeclineChallenge(id: Long) {
        viewModelScope.launch {
            state = NotificationState.DECLINE_CHALLENGE
            // create use case to decline
            state = NotificationState.IDLE
        }
    }
}