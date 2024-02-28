package com.pedfu.daystreak.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pedfu.daystreak.data.local.category.CategoryDao
import com.pedfu.daystreak.data.local.category.CategoryEntity
import com.pedfu.daystreak.data.local.streak.StreakDao
import com.pedfu.daystreak.data.local.streak.StreakEntity
import com.pedfu.daystreak.data.local.user.UserDao
import com.pedfu.daystreak.data.local.user.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        StreakEntity::class,
        CategoryEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun streakDao(): StreakDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "day_streak_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}