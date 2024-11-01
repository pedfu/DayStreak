package com.pedfu.daystreak.presentation.creation.streak

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.streak.StreakRequest
import com.pedfu.daystreak.data.repositories.streak.StreakRepository
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.presentation.creation.streak.backgroundOptions.BackgroundOption
import com.pedfu.daystreak.usecases.streak.StreakUseCase
import com.pedfu.daystreak.utils.ImageProvider
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
    private val streakId: Long? = null,
    private val context: Context? = null,
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
    private var streakBackgroundLocal: BackgroundOption? = null
        set(value) {
            field = value
            streakBackgroundLocalLiveData.value = value
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
    val streakBackgroundLocalLiveData = MutableLiveData(streakBackgroundLocal)
    val streakDescriptionLiveData = MutableLiveData(streakDescription)

    val streakLiveData = MutableLiveData<StreakItem?>(null)

    val stateLiveData = MutableLiveData(state)
    val errorLiveData = MutableLiveData<List<StreakCreationFields>>(emptyList())

    init {
        viewModelScope.launch {
            val categories = streakRepository.getAllCategories()
            categoriesLiveData.value = categories
            if (streakId != null) {
                val streak = streakRepository.getStreak(streakId)
                streakLiveData.value = streak
                if (streak != null) {
                    streakName = streak.name
//                    streakGoalDeadline = streak.e
//                    streakMinTimePerDayInMinutes = streak.

                    streakCategory = categories.find { it.id == streak.categoryId }
//                    streakBackgroundImage = streak.backgroundPicture
//                    streakBackgroundColor = streak.color
                    streakBackgroundLocal = if (streak.localBackgroundPicture != null && context != null) BackgroundOption(streak.localBackgroundPicture, ImageProvider.loadLocalImage(streak.localBackgroundPicture, context)) else null
                    streakDescription = streak.description ?: ""
                }
            }
        }
    }

    fun onStreakNameChanged(name: String) = run { streakName = name }
    fun onStreakGoalDeadlineChanged(goalDeadLine: Date) = run { streakGoalDeadline = goalDeadLine }
    fun onStreakMinTimePerDayInMinutesChanged(minTimePerDay: Int) = run { streakMinTimePerDayInMinutes = minTimePerDay }
    fun onStreakCategoryChanged(category: StreakCategoryItem) = run { streakCategory = category }
    fun onStreakBackgroundImageChanged(backgroundPicture: File) = run { streakBackgroundImage = backgroundPicture }
    fun onStreakBackgroundColorChanged(color: String) = run { streakBackgroundColor = color }
    fun onStreakDescriptionChanged(description: String) = run { streakDescription = description }
    fun onSelectLocalImage(name: String, imageRes: Int) = run {
        streakBackgroundLocal = BackgroundOption(name, imageRes)
    }


    private fun validateFields() {
        val ready = !streakName.isNullOrBlank() && streakCategory != null && !streakDescription.isNullOrBlank()

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
                    streakBackgroundLocal?.name
                )

                if (streakId != null) streakUseCase.updateStreak(streakId, request)
                else streakUseCase.createStreak(request)
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
        streakBackgroundLocal = null
    }
}