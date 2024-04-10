package com.pedfu.daystreak.usecases.streak

import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.streak.CategoryRequest
import com.pedfu.daystreak.data.remote.streak.StreakService
import com.pedfu.daystreak.data.repositories.streak.StreakRepository
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
import com.pedfu.daystreak.domain.streak.StreakItem

class StreakUseCase(
    private val streakService: StreakService = Inject.streakService,
    private val streakRepository: StreakRepository = Inject.streakRepository,
) {
    suspend fun fetchStreaks() {
        streakService.getStreaks().forEach {
            // save category
            val category = it.category.toCategory()
            saveCategory(category)
            saveStreak(it.toStreak())
        }
    }

    suspend fun saveStreak(streak: StreakItem) {
        streakRepository.saveStreak(streak)
    }

    suspend fun saveCategory(category: StreakCategoryItem) {
        streakRepository.saveCategory(category)
    }

    suspend fun createCategory(category: CategoryRequest) {
        // api call
        // save repo if api call succeed
    }
}