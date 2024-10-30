package com.pedfu.daystreak.presentation.creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.repositories.streak.StreakRepository

class SelectCreateTypeViewModel(
    private val streakRepository: StreakRepository = Inject.streakRepository,
): ViewModel() {
    val categoriesLiveData = streakRepository.categoriesFlow.asLiveData()
}