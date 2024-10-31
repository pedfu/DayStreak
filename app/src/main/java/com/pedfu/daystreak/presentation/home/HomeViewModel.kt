package com.pedfu.daystreak.presentation.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.repositories.notification.NotificationRepository
import com.pedfu.daystreak.data.repositories.streak.StreakRepository
import com.pedfu.daystreak.data.repositories.user.UserRepository
import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.domain.user.User
import com.pedfu.daystreak.utils.ImageProvider

enum class HomeState {
    IDLE,
    LOADING,
    CREATING_CATEGORY,
    CREATING_STREAK
}

class HomeViewModel(
    private val context: Context,
    private val userRepository: UserRepository = Inject.userRepository,
    private val streakRepository: StreakRepository = Inject.streakRepository,
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
    val streaksLiveData = streakRepository.streaksFlow.asLiveData().map {
        it.map { item ->
            if (item.localBackgroundPicture != null) {
                StreakItem(item, ImageProvider.loadOptimizedLocalImage(item.localBackgroundPicture, context))
            } else {
                item
            }
        }
    }
    val categoriesLiveData = streakRepository.categoriesFlow.asLiveData()
    val notificationsLiveData = notificationRepository.notificationsFlow.asLiveData()
    val filteredStreaksLiveData = MutableLiveData(filteredStreaks)
    val selectedCategoryLiveData = MutableLiveData(selectedCategory)

    fun onSelectCategory(id: Long) {
        selectedCategory = id
        filterStreaks()
    }

    fun filterStreaks() {
        filteredStreaks = streaksLiveData.value?.filter { it.categoryId == selectedCategory } ?: emptyList()
    }
}