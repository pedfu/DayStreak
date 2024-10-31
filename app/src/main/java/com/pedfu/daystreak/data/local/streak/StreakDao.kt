package com.pedfu.daystreak.data.local.streak

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.pedfu.daystreak.domain.streak.StreakItem
import kotlinx.coroutines.flow.Flow

@Dao
interface StreakDao {
    @Query("SELECT * FROM streak")
    suspend fun getAll(): List<StreakEntity>

    @Query("SELECT * FROM streak WHERE categoryId = :categoryId")
    suspend fun getByCategoryId(categoryId: Long): List<StreakEntity>

    @Query("SELECT * FROM streak WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): StreakEntity?

    @Query("SELECT * FROM streak WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<StreakEntity?>

    @Query("SELECT * FROM streak")
    fun observe(): Flow<List<StreakEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(streak: StreakEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(streak: StreakEntity)

    suspend fun refreshStreaks(streaks: List<StreakItem>) {
        val allStreaks = getAll()

        // Remove non existing (or removed) streaks
        val streaksToRemove = allStreaks.filter { streaks.any { c -> c.id != it.id } }
        streaksToRemove.forEach { if (it.id != null) delete(it.id) }

        streaks.forEach { streak ->
            insertWithTransaction(streak)
        }
    }

    @Transaction
    suspend fun insertWithTransaction(streak: StreakItem) {
        runCatching {
            insert(StreakEntity(streak))
        }
    }

    @Transaction
    @Query("DELETE FROM streak")
    suspend fun deleteAll()

    @Transaction
    @Query("DELETE FROM streak WHERE id = :id")
    suspend fun delete(id: Long)

    @Transaction
    @Query("DELETE FROM streak WHERE categoryId = :categoryId")
    suspend fun deleteByCategoryId(categoryId: Long)
}