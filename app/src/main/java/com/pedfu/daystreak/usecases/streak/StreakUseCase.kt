package com.pedfu.daystreak.usecases.streak

import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.streak.CategoryRequest
import com.pedfu.daystreak.data.remote.streak.StreakRequest
import com.pedfu.daystreak.data.remote.streak.StreakService
import com.pedfu.daystreak.data.repositories.streak.StreakRepository
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
import com.pedfu.daystreak.domain.streak.StreakItem

class StreakUseCase(
    private val streakService: StreakService = Inject.streakService,
    private val streakRepository: StreakRepository = Inject.streakRepository,
) {
    suspend fun fetchStreaks() {
        val streaks = streakService.getStreaks()
        streakRepository.onRefreshStreak(streaks.map { it.toStreak() })
    }

    suspend fun fetchCategories() {
        val categories = streakService.getCategories()
        streakRepository.onRefreshCategory(categories.map { it.toCategory() })
    }

    suspend fun createCategory(category: CategoryRequest) {
        val categoryResponse = streakService.createCategory(category)
        streakRepository.saveCategory(categoryResponse.toCategory())
    }

    suspend fun createStreak(streak: StreakRequest) {
        val streakResponse = streakService.createStreak(streak) ?: return
        streakRepository.saveStreak(streakResponse.toStreak())
    }

    suspend fun updateStreak(id: Long, streak: StreakRequest) {
        val streakResponse = streakService.updateStreak(id, streak) ?: return
        streakRepository.updateStreak(id, streakResponse.toStreak())
    }

    suspend fun deleteCategory(categoryId: Long) {
        val result = streakService.deleteCategory(categoryId)
        if (result) streakRepository.deleteCategory(categoryId)
    }

    suspend fun deleteStreak(streakId: Long) {
        val result = streakService.deleteStreak(streakId)
        if (result) streakRepository.deleteStreak(streakId)
    }
}