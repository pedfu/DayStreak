package com.pedfu.daystreak.data.local.category

import androidx.room.Dao
import androidx.room.Ignore
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): CategoryEntity?

    @Query("SELECT * FROM category")
    suspend fun getAllCategories(): List<CategoryEntity>

    @Query("SELECT * FROM category")
    suspend fun getAll(): List<CategoryEntity>

    @Query("SELECT * FROM category WHERE id = :id LIMIT 1")
    suspend fun getCategory(id: Long): CategoryEntity?

    @Query("SELECT * FROM category WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<CategoryEntity?>

    @Query("SELECT * FROM category")
    fun observe(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(streak: CategoryEntity)

    @Update
    suspend fun update(category: CategoryEntity)

    @Transaction
    suspend fun refreshCategories(categories: List<StreakCategoryItem>) {
        val allCategories = getAllCategories()

        // Remove non existing (or removed) categories
        val categoriesToRemove = if (categories.isEmpty()) allCategories else allCategories.filter {categories.any { c -> c.id != it.id }}
        categoriesToRemove.forEach { delete(it.id) }

        // Update existing categories
        categories.forEach { insert(CategoryEntity(it.id, it.name)) }
    }

    @Transaction
    @Query("DELETE FROM category")
    suspend fun deleteAll()

    @Transaction
    @Query("DELETE FROM category WHERE id = :id")
    suspend fun delete(id: Long)
}