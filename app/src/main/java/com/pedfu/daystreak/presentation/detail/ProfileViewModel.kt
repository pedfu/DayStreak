package com.pedfu.daystreak.presentation.profile

import android.app.Dialog
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

enum class ProfileState {
    IDLE,
    DELETING,
    LOADING,
    DELETED,
}

class ProfileViewModel(): ViewModel() {
    private var state = ProfileState.IDLE
        set(value) {
            field = value
            stateLiveData.value = value
        }
    val stateLiveData = MutableLiveData(state)
}