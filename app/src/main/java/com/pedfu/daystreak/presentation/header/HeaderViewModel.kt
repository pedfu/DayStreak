package com.pedfu.daystreak.presentation.header

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class HeaderState {
    IDLE,
    LOADING
}

class HeaderViewModel : ViewModel() {
    private var state = HeaderState.IDLE
        set(value) {
            field = value
        }

    val stateLiveData = MutableLiveData(state)

    init {

    }
}