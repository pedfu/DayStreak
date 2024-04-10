package com.pedfu.daystreak.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.streak.CategoryRequest
import com.pedfu.daystreak.data.repositories.notification.NotificationRepository
import com.pedfu.daystreak.data.repositories.streak.StreakRepository
import com.pedfu.daystreak.data.repositories.user.UserRepository
import com.pedfu.daystreak.domain.user.User
import com.pedfu.daystreak.usecases.streak.StreakUseCase
import kotlinx.coroutines.launch

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
    // default = emptyList()
    private var state: HomeState = HomeState.IDLE
        set(value) {
            field = value
            stateLiveData.value = value
        }
    private var selectedCategory: Long = 0
        set(value) {
            field = value
            selectedCategoryLiveData.value = value
        }
    private var newCategoryName: String = ""

    val stateLiveData = MutableLiveData(state)
    val userLiveData: LiveData<User?> = userRepository.userFlow.asLiveData()
    val streaksLiveData = streakRepository.streaksFlow.asLiveData()
    val categoriesLiveData = streakRepository.categoriesFlow.asLiveData()
    val notificationsLiveData = notificationRepository.notificationsFlow.asLiveData()
    val selectedCategoryLiveData = MutableLiveData(selectedCategory)
    val categoryFormErrorLiveData = MutableLiveData<String?>(null)

    fun onCategoryNameChanged(text: String) {
        newCategoryName = text
    }

    fun onCreateCategory() {
        if (newCategoryName.isNullOrBlank()) return

        viewModelScope.launch {
            state = HomeState.CREATING_CATEGORY
            streakUseCase.createCategory(CategoryRequest(null, newCategoryName))
            newCategoryName = ""
            state = HomeState.IDLE

            // on error - show tooltip on page
        }
    }
}