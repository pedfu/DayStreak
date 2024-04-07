package com.pedfu.daystreak.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.local.user.UserEntity
import com.pedfu.daystreak.data.repositories.notification.NotificationRepository
import com.pedfu.daystreak.data.repositories.streak.StreakRepository
import com.pedfu.daystreak.data.repositories.user.UserRepository
import com.pedfu.daystreak.domain.notification.NotificationItem
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.domain.streak.StreakStatus
import com.pedfu.daystreak.domain.user.User
import java.util.Date

class HomeViewModel(
    private val userRepository: UserRepository = Inject.userRepository,
    private val streakRepository: StreakRepository = Inject.streakRepository,
    private val notificationRepository: NotificationRepository = Inject.notificationRepository,
): ViewModel() {
    // default = emptyList()
    private var selectedCategory: Long = 0
        set(value) {
            field = value
            selectedCategoryLiveData.value = value
        }

    val userLiveData: LiveData<User?> = userRepository.userFlow.asLiveData()
    val streaksLiveData = streakRepository.streaksFlow.asLiveData()
    val categoriesLiveData = streakRepository.categoriesFlow.asLiveData()
    val notificationsLiveData = notificationRepository.notificationsFlow.asLiveData()
    val selectedCategoryLiveData = MutableLiveData(selectedCategory)
}