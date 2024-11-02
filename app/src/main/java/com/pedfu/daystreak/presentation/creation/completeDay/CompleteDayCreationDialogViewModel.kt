package com.pedfu.daystreak.presentation.creation.completeDay

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.streak.CompleteDayRequest
import com.pedfu.daystreak.data.repositories.streak.StreakRepository
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.usecases.streak.StreakUseCase
import kotlinx.coroutines.launch
import java.util.Date

const val EMPTY_NAME = "empty_name"
const val EXISTING_NAME = "existing_name"
const val NETWORK = "network"

enum class CompleteDayCreationState {
    IDLE,
    READY,
    LOADING,
    DONE,
}

class CompleteDayCreationDialogViewModel(
    private val streakId: Long,
    private val streakRepository: StreakRepository = Inject.streakRepository,
    private val streakUseCase: StreakUseCase = Inject.streakUseCase,
) : ViewModel() {
    private var streak: StreakItem? = null
    private var streakDayTimeInMinutes: Int = 0
        set(value) {
            field = value
            streakDayTimeInMinutesLiveData.value = value
            validateFields()
        }
    private var streakDayDescription: String = ""
        set(value) {
            field = value
            streakDayDescriptionLiveData.value = value
            validateFields()
        }
    private var streakDayDate: Date? = null
        set(value) {
            field = value
            streakDayDateLiveData.value = value
            validateFields()
        }
    private var state: CompleteDayCreationState = CompleteDayCreationState.IDLE
        set(value) {
            field = value
            stateLiveData.value = value
        }

    val streakDayTimeInMinutesLiveData = MutableLiveData(streakDayTimeInMinutes)
    val streakDayDescriptionLiveData = MutableLiveData(streakDayDescription)
    val streakDayDateLiveData = MutableLiveData(streakDayDate)
    val stateLiveData = MutableLiveData(state)
    val errorLiveData = MutableLiveData<String?>(null)

    private var existingCategories: List<StreakCategoryItem> = emptyList()

    init {
        viewModelScope.launch {
            existingCategories = streakRepository.getAllCategories()
            streak = streakRepository.getStreak(streakId)
        }
    }

    fun onStreakDayTimeInMinutesChanged(min: Int) {
        streakDayTimeInMinutes = min
    }
    fun onStreakDescriptionChanged(text: String) {
        streakDayDescription = text
    }
    fun onStreakDateChanged(date: Date) {
        streakDayDate = date
    }

    private fun validateFields() {
        errorLiveData.value = null
        stateLiveData.value = CompleteDayCreationState.READY
    }

    fun onFinish() {
        if (stateLiveData.value != CompleteDayCreationState.READY) return

        // create category
        viewModelScope.launch {
            try {
                state = CompleteDayCreationState.LOADING
                streakUseCase.completeStreakDay(CompleteDayRequest(streakId, streakDayDate, streakDayTimeInMinutes, streakDayDescription))
                state = CompleteDayCreationState.DONE
            } catch (ex: Throwable) {
                errorLiveData.value = NETWORK
            }
        }
    }

    fun resetData() {
        streakDayTimeInMinutes = 0
        streakDayDate = null
        streakDayDescription = ""
    }
}