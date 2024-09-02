package com.pedfu.daystreak.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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