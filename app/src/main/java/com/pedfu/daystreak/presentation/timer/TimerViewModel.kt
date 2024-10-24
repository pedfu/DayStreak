package com.pedfu.daystreak.presentation.timer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.usecases.timer.TimerUseCase
import kotlinx.coroutines.launch
import java.util.Date

enum class TimerState {
    IDLE,
    RUNNING,
    PAUSED,
    LOADING
}

class TimerViewModel(
    private val minutes: Long,
    private val timerUseCase: TimerUseCase = Inject.timerUseCase,
): ViewModel() {
    private var state: TimerState = TimerState.IDLE

    private var first = true
    private var startTime: Date = Date()
    private var endTime: Date = Date()
    private var durationInSec: Long = 0
    var totalTimer: Long = minutes * 60
        private set
    var timeLeft: Long = totalTimer
        private set

    val stateLiveData = MutableLiveData(state)
    val totalTimerLiveData = MutableLiveData(totalTimer)

    // create functionality to edit the total timer
    fun updateTotalTimer(timeInSec: Long) {
        totalTimer = timeInSec
    }

    fun updateTimeLeft(timeInSec: Long) {
        timeLeft = timeInSec
    }

    fun startTimer() {
        startTime = Date()
        state = TimerState.RUNNING
    }

    fun stopTimer() {
        endTime = Date()
        durationInSec = totalTimer
    }

    fun onConfirmCreateTrack() {
        if (state != TimerState.IDLE) return

        viewModelScope.launch {
            state = TimerState.LOADING
            timerUseCase.sendNewTrack(durationInSec, startTime, endTime)
            state = TimerState.IDLE
        }
    }
}