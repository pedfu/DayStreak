package com.pedfu.daystreak.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pedfu.daystreak.data.local.badge.BadgeEntity
import com.pedfu.daystreak.data.local.category.CategoryDao
import com.pedfu.daystreak.data.local.category.CategoryEntity
import com.pedfu.daystreak.data.local.notification.NotificationDao
import com.pedfu.daystreak.data.local.notification.NotificationEntity
import com.pedfu.daystreak.data.local.streak.StreakDao
import com.pedfu.daystreak.data.local.streak.StreakEntity
import com.pedfu.daystreak.data.local.user.UserDao
import com.pedfu.daystreak.data.local.user.UserEntity
import com.pedfu.daystreak.utils.converters.DateConverter

@Database(
    entities = [
        UserEntity::class,
        StreakEntity::class,
        CategoryEntity::class,
        NotificationEntity::class,
        BadgeEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun streakDao(): StreakDao
    abstract fun categoryDao(): CategoryDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "day_streak_database"
                )
                    .apply {
                        fallbackToDestructiveMigration()
                    }
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}