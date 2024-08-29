package com.pedfu.daystreak.presentation.creation.completeDay

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.streak.CategoryRequest
import com.pedfu.daystreak.data.remote.streak.CompleteDayRequest
import com.pedfu.daystreak.data.repositories.streak.StreakRepository
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
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
    private var streakDayHours: String = ""
        set(value) {
            field = value
            streakDayHoursLiveData.value = value
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

    val streakDayHoursLiveData = MutableLiveData(streakDayHours)
    val streakDayDescriptionLiveData = MutableLiveData(streakDayDescription)
    val streakDayDateLiveData = MutableLiveData(streakDayDate)
    val stateLiveData = MutableLiveData(state)
    val errorLiveData = MutableLiveData<String?>(null)

    private var existingCategories: List<StreakCategoryItem> = emptyList()

    init {
        viewModelScope.launch {
            existingCategories = streakRepository.getAllCategories()
        }
    }

    fun onStreakHoursChanged(hours: String) {
        streakDayHours = hours
    }
    fun onStreakDescriptionChanged(text: String) {
        streakDayDescription = text
    }
    fun onStreakDateChanged(date: Date) {
        streakDayDate = date
    }

    private fun validateFields() {
        if (streakDayHours?.isNullOrEmpty() == true || streakDayHours.replace(":", "").length < 4) {
            errorLiveData.value = EMPTY_NAME
            stateLiveData.value = CompleteDayCreationState.IDLE
            return
        }

        errorLiveData.value = null
        stateLiveData.value = CompleteDayCreationState.READY
    }

    private fun getTimeInMinutes(): Int {
        val cleanTxt = streakDayHours.replace(":", "")
        val hh = cleanTxt.substring(0, 2).toInt() * 60
        val mm = cleanTxt.substring(2, 4).toInt()
        return hh + mm
    }

    fun onFinish() {
        if (stateLiveData.value != CompleteDayCreationState.READY) return

        // create category
        viewModelScope.launch {
            try {
                state = CompleteDayCreationState.LOADING
                val minutes = getTimeInMinutes()
                streakUseCase.completeStreakDay(CompleteDayRequest(streakId, streakDayDate, minutes, streakDayDescription))
                state = CompleteDayCreationState.DONE
            } catch (ex: Throwable) {
                errorLiveData.value = NETWORK
            }
        }
    }

    fun resetData() {
        streakDayHours = ""
        streakDayDate = null
        streakDayDescription = ""
    }
}