package com.pedfu.daystreak.presentation.creation.streak

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.streak.CategoryRequest
import com.pedfu.daystreak.data.remote.streak.StreakRequest
import com.pedfu.daystreak.data.repositories.streak.StreakRepository
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
import com.pedfu.daystreak.presentation.home.HomeViewModel
import com.pedfu.daystreak.usecases.streak.StreakUseCase
import kotlinx.coroutines.launch
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date

const val EMPTY_NAME = "empty_name"
const val EXISTING_NAME = "existing_name"

enum class StreakCreationState {
    IDLE,
    READY,
    LOADING,
    DONE,
}

enum class StreakCreationFields {
    NAME,
    GOAL_DEADLINE,
    MIN_TIME_PER_DAY,
    CATEGORY,
    BACKGROUND_IMAGE,
    BACKGROUND_COLOR,
    DESCRIPTION,
    NETWORK,
}

class StreakCreationDialogViewModel(
    private val streakRepository: StreakRepository = Inject.streakRepository,
    private val streakUseCase: StreakUseCase = Inject.streakUseCase,
) : ViewModel() {
    private var state: StreakCreationState = StreakCreationState.IDLE
        set(value) {
            field = value
            stateLiveData.value = value
        }

    private var streakName: String = ""
        set(value) {
            field = value
            streakNameLiveData.value = value
            validateFields()
        }
    private var streakGoalDeadline: Date? = null
        set(value) {
            field = value
            streakGoalDeadlineLiveData.value = value
            validateFields()
        }
    private var streakMinTimePerDayInMinutes: Int = 0
        set(value) {
            field = value
            streakMinTimePerDayInMinutesLiveData.value = value
            validateFields()
        }
    private var streakCategory: StreakCategoryItem? = null
        set(value) {
            field = value
            streakCategoryLiveData.value = value
            validateFields()
        }
    private var streakBackgroundImage: File? = null
        set(value) {
            field = value
            streakBackgroundImageLiveData.value = value
            validateFields()
        }
    private var streakBackgroundColor: String? = null
        set(value) {
            field = value
            streakBackgroundColorLiveData.value = value
            validateFields()
        }
    private var streakDescription: String = ""
        set(value) {
            field = value
            streakDescriptionLiveData.value = value
            validateFields()
        }

    val categoriesLiveData = MutableLiveData<List<StreakCategoryItem>>()
    val streakNameLiveData = MutableLiveData(streakName)
    val streakGoalDeadlineLiveData = MutableLiveData(streakGoalDeadline)
    val streakMinTimePerDayInMinutesLiveData = MutableLiveData(streakMinTimePerDayInMinutes)
    val streakCategoryLiveData = MutableLiveData(streakCategory)
    val streakBackgroundImageLiveData = MutableLiveData(streakBackgroundImage)
    val streakBackgroundColorLiveData = MutableLiveData(streakBackgroundColor)
    val streakDescriptionLiveData = MutableLiveData(streakDescription)

    val stateLiveData = MutableLiveData(state)
    val errorLiveData = MutableLiveData<List<StreakCreationFields>>(emptyList())

    init {
        viewModelScope.launch {
            categoriesLiveData.value = streakRepository.getAllCategories()
        }
    }

    fun onStreakNameChanged(name: String) = run { streakName = name }
    fun onStreakGoalDeadlineChanged(goalDeadLine: Date) = run { streakGoalDeadline = goalDeadLine }
    fun onStreakMinTimePerDayInMinutesChanged(minTimePerDay: Int) = run { streakMinTimePerDayInMinutes = minTimePerDay }
    fun onStreakCategoryChanged(category: StreakCategoryItem) = run { streakCategory = category }
    fun onStreakBackgroundImageChanged(backgroundPicture: File) = run { streakBackgroundImage = backgroundPicture }
    fun onStreakBackgroundColorChanged(color: String) = run { streakBackgroundColor = color }
    fun onStreakDescriptionChanged(description: String) = run { streakDescription = description }


    private fun validateFields() {
        val ready = !streakName.isNullOrBlank() && streakGoalDeadline != null &&
                streakMinTimePerDayInMinutes >= 0 && streakCategory != null && !streakDescription.isNullOrBlank()

        if (ready) {
            errorLiveData.value = emptyList()
            stateLiveData.value = StreakCreationState.READY
        } else {
            stateLiveData.value = StreakCreationState.IDLE
        }
    }

    private fun validateOnFinish(): Boolean {
        val errors = mutableListOf<StreakCreationFields>()
        if (streakName.isNullOrBlank()) errors.add(StreakCreationFields.NAME)
        if (streakGoalDeadline == null) errors.add(StreakCreationFields.GOAL_DEADLINE)
        if (streakMinTimePerDayInMinutes < 0) errors.add(StreakCreationFields.MIN_TIME_PER_DAY)
        if (streakCategory == null) errors.add(StreakCreationFields.CATEGORY)
        if (streakDescription.isNullOrBlank()) errors.add(StreakCreationFields.DESCRIPTION)

        errorLiveData.value = errors
        if (errors.size > 0) return false
        return true
    }

    fun onFinish() {
        if (stateLiveData.value != StreakCreationState.READY && !validateOnFinish()) return

        // create category
        viewModelScope.launch {
            try {
                state = StreakCreationState.LOADING
                val durationDays: Long? = if (streakGoalDeadline != null) {
                    val today = Calendar.getInstance()
                    val deadline = Calendar.getInstance().apply { time = streakGoalDeadline }
                    val todayLocalDate = LocalDate.of(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(
                        Calendar.DAY_OF_MONTH))
                    val deadlineLocalDate = LocalDate.of(deadline.get(Calendar.YEAR), deadline.get(
                        Calendar.MONTH) + 1, deadline.get(Calendar.DAY_OF_MONTH))
                    ChronoUnit.DAYS.between(todayLocalDate, deadlineLocalDate)
                } else {
                    null
                }
                val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val goalDeadline = if (streakGoalDeadline != null) {
                    Instant.ofEpochMilli(streakGoalDeadline!!.time).atZone(ZoneId.systemDefault()).toLocalDate()
                } else {
                    null
                }
                val formattedDate = goalDeadline?.format(dateFormat)
                val request = StreakRequest(
                    streakName,
                    streakDescription,
                    durationDays,
                    formattedDate,
                    streakBackgroundImage,
                    streakCategory?.id,
                    streakMinTimePerDayInMinutes,
                )
                streakUseCase.createStreak(request)
                state = StreakCreationState.DONE
            } catch (ex: Throwable) {
                errorLiveData.value = listOf(StreakCreationFields.NETWORK)
            }
        }
    }

    fun resetData() {
        streakName = ""
        streakGoalDeadline = null
        streakMinTimePerDayInMinutes = 0
        streakCategory = null
        streakBackgroundImage = null
        streakBackgroundColor = null
        streakDescription = ""
    }
}