package com.pedfu.daystreak.presentation.header.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.repositories.notification.NotificationRepository
import com.pedfu.daystreak.domain.notification.NotificationItem
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
    private val notificationRepository: NotificationRepository = Inject.notificationRepository
) : ViewModel() {

    private var state: NotificationState = NotificationState.IDLE
        set(value) {
            field = value
        }

    val notificationsLiveData = notificationRepository.notificationsFlow.asLiveData()
    val stateLiveData = MutableLiveData(state)

    fun markAllAsRead() {
        viewModelScope.launch {
            state = NotificationState.MARKING_READ
            notificationRepository.markAllAsRead()
            state = NotificationState.IDLE
        }
    }

    fun cleanAll() {
        viewModelScope.launch {
            state = NotificationState.CLEARING
            notificationRepository.removeAll()
            state = NotificationState.IDLE
        }
    }

    fun markItemRead(id: Long) {
        viewModelScope.launch {
            state = NotificationState.MARKING_READ
            notificationRepository.markAsRead(id)
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