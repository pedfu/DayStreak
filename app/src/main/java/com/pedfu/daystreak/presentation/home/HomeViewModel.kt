package com.pedfu.daystreak.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.streak.CategoryRequest
import com.pedfu.daystreak.data.remote.streak.StreakRequest
import com.pedfu.daystreak.data.repositories.notification.NotificationRepository
import com.pedfu.daystreak.data.repositories.streak.StreakRepository
import com.pedfu.daystreak.data.repositories.user.UserRepository
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.domain.user.User
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

enum class HomeState {
    IDLE,
    LOADING,
    CREATING_CATEGORY,
    CREATING_STREAK
}

class HomeViewModel(
    private val userRepository: UserRepository = Inject.userRepository,
    private val streakRepository: StreakRepository = Inject.streakRepository,
    private val streakUseCase: StreakUseCase = Inject.streakUseCase,
    private val notificationRepository: NotificationRepository = Inject.notificationRepository,
): ViewModel() {
    private var state: HomeState = HomeState.IDLE
        set(value) {
            field = value
            stateLiveData.value = value
        }
    private var selectedCategory: Long = -1
        set(value) {
            field = value
            selectedCategoryLiveData.value = value
        }
    private var filteredStreaks: List<StreakItem> = emptyList()
        set(value) {
            field = value
            filteredStreaksLiveData.value = value
        }

    val stateLiveData = MutableLiveData(state)
    val userLiveData: LiveData<User?> = userRepository.userFlow.asLiveData()
    val streaksLiveData = streakRepository.streaksFlow.asLiveData()
    val categoriesLiveData = streakRepository.categoriesFlow.asLiveData()
    val notificationsLiveData = notificationRepository.notificationsFlow.asLiveData()
    val filteredStreaksLiveData = MutableLiveData(filteredStreaks)
    val selectedCategoryLiveData = MutableLiveData(selectedCategory)

    // category form modal
    private var newCategoryName: String = ""
    val categoryFormErrorLiveData = MutableLiveData<String?>(null)

    // streak form modal
    private var newStreakName: String = ""
    private var newStreakGoalDeadline: Date? = null
    private var newStreakMinTimePerDay: Int = 0
    private var newStreakBackgroundPicture: File? = null
    private var newStreakDescription: String = ""
    private var newStreakCategory: StreakCategoryItem? = null

    fun onSelectCategory(id: Long) {
        selectedCategory = id
        filterStreaks()
    }

    fun filterStreaks() {
        filteredStreaks = streaksLiveData.value?.filter { it.categoryId == selectedCategory } ?: emptyList()
    }

    fun onCategoryNameChanged(text: String) { newCategoryName = text }
    fun onStreakNameChanged(text: String) { newStreakName = text }
    fun onStreakGoalDeadlineChanged(endDate: Date) { newStreakGoalDeadline = endDate }
    fun onStreakMinTimePerDayChanged(minTimePerDay: String) {
        try {
            newStreakMinTimePerDay = minTimePerDay.toInt()
        } catch (ex: Throwable) {}
    }
    fun onStreakBackgroundPictureChanged(backgroundPicture: File) { newStreakBackgroundPicture = backgroundPicture }
    fun onStreakDescriptionChanged(text: String) { newStreakDescription = text }
    fun onStreakCategoryChanged(category: StreakCategoryItem) {
        newStreakCategory = category
    }

    fun onCreateCategory(closeDialog: () -> Unit) {
        if (newCategoryName.isNullOrBlank()) return

        viewModelScope.launch {
            state = HomeState.CREATING_CATEGORY
            streakUseCase.createCategory(CategoryRequest(null, newCategoryName))
            newCategoryName = ""
            state = HomeState.IDLE

            closeDialog()
            // on error - show tooltip on page
        }
    }

    fun onCreateStreak(closeDialog: () -> Unit) {
        if (
            newStreakName.isNullOrBlank() ||
            newStreakCategory == null ||
            newStreakBackgroundPicture == null ||
            newStreakDescription.isNullOrBlank()) return

        val categoryId = newStreakCategory?.id ?: return

        viewModelScope.launch {
            state = HomeState.CREATING_STREAK
            val durationDays: Long? = if (newStreakGoalDeadline != null) {
                val today = Calendar.getInstance()
                val deadline = Calendar.getInstance().apply { time = newStreakGoalDeadline }
                val todayLocalDate = LocalDate.of(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH))
                val deadlineLocalDate = LocalDate.of(deadline.get(Calendar.YEAR), deadline.get(Calendar.MONTH) + 1, deadline.get(Calendar.DAY_OF_MONTH))
                ChronoUnit.DAYS.between(todayLocalDate, deadlineLocalDate)
            } else {
                null
            }
            val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val goalDeadline = if (newStreakGoalDeadline != null) {
                Instant.ofEpochMilli(newStreakGoalDeadline!!.time).atZone(ZoneId.systemDefault()).toLocalDate()
            } else {
                null
            }
            val formattedDate = goalDeadline?.format(dateFormat)
            val request = StreakRequest(
                id = null,
                name = newStreakName,
                description = newStreakDescription,
                durationDays = durationDays,
                endDate = formattedDate,
                background = newStreakBackgroundPicture!!,
                category = null,
                categoryId = categoryId,
                minTimePerDay = newStreakMinTimePerDay,
            )
            streakUseCase.createStreak(request)
            state = HomeState.IDLE

            closeDialog()
            // on error - show tooltip on page
        }
    }
}