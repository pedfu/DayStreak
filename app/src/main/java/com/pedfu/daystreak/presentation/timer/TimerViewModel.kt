package com.pedfu.daystreak.presentation.timer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.streak.CompleteDayRequest
import com.pedfu.daystreak.usecases.streak.StreakUseCase
import kotlinx.coroutines.launch
import java.util.Date

enum class TimerState {
    IDLE,
    RUNNING,
    PAUSED,
    LOADING,
    TIME_SAVED,
}

class TimerViewModel(
    private val minutes: Long,
    private val streakId: Long,
    private val streakUseCase: StreakUseCase = Inject.streakUseCase,
): ViewModel() {
    private var state: TimerState = TimerState.IDLE
        set(value) {
            field = value
            stateLiveData.value = value
        }

    private var startTime: Date = Date()
    private var endTime: Date = Date()
    private var durationInSec: Long = 0
    var totalTimer: Long = minutes * 60
        private set(value) {
            field = value
            totalTimerLiveData.value = value
        }
    var timeLeft: Long = totalTimer
        private set

    val stateLiveData = MutableLiveData(state)
    val totalTimerLiveData = MutableLiveData(totalTimer)

    // create functionality to edit the total timer
    fun updateTotalTimer(timeInSec: String) {
        val minutes = timeInSec.substring(0, 2).toInt() * 60
        val seconds = timeInSec.substring(2, 4).toInt()
        totalTimer = (minutes + seconds).toLong()
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
        state = TimerState.IDLE
    }

    fun onConfirmCreateTrack() {
        if (state != TimerState.IDLE) return

        viewModelScope.launch {
            state = TimerState.LOADING

            val seconds = (totalTimer - timeLeft).toInt()
            // date = null => today
            streakUseCase.completeStreakDay(CompleteDayRequest(streakId, null, seconds, "Using timer"))
            state = TimerState.TIME_SAVED
        }
    }
}