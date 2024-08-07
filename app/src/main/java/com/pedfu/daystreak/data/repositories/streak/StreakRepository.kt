package com.pedfu.daystreak.data.repositories.streak

import android.net.Uri
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.local.category.CategoryDao
import com.pedfu.daystreak.data.local.category.CategoryEntity
import com.pedfu.daystreak.data.local.streak.StreakDao
import com.pedfu.daystreak.data.local.streak.StreakEntity
import com.pedfu.daystreak.data.local.user.UserDao
import com.pedfu.daystreak.data.local.user.UserEntity
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.domain.streak.StreakStatus
import com.pedfu.daystreak.domain.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StreakRepository(
    private val streakDao: StreakDao = Inject.streakDao,
    private val categoryDao: CategoryDao = Inject.categoryDao,
) {
    val streaksFlow: Flow<List<StreakItem>> = streakDao.observe().map {
        it.map { s -> s.toStreak() }
    }
    val categoriesFlow: Flow<List<StreakCategoryItem>> = categoryDao.observe().map {
        it.map { s -> s.toCategory() }
    }

    suspend fun getStreak(id: Long): StreakItem? {
        return streakDao.findById(id)?.toStreak()
    }

    suspend fun getAllStreaks(): List<StreakItem> {
        return streakDao.getAll().map { it.toStreak() }
    }

    suspend fun onRefreshStreak(streaks: List<StreakItem>) {
        streakDao.refreshStreaks(streaks)
    }

    suspend fun onRefreshCategory(categories: List<StreakCategoryItem>) {
        categoryDao.refreshCategories(categories)
    }

    suspend fun saveStreak(streak: StreakItem) {
        streakDao.insert(StreakEntity(streak))
    }

    suspend fun saveCategory(category: StreakCategoryItem) {
        categoryDao.insert(CategoryEntity(category.id, category.name))
    }

    suspend fun clear() {
        streakDao.deleteAll()
    }
}
