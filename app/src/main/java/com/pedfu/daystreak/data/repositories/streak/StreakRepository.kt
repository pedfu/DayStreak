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
    val streaksFlow: Flow<List<StreakItem?>> = streakDao.observe().map { it.map { s -> s?.toStreak() } }

    suspend fun getStreak(): StreakItem? {
        return streakDao.findById(1L)?.toStreak()
    }

    suspend fun getAllStreaks(): List<StreakItem> {
        return streakDao.getAll().map { it.toStreak() }
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
