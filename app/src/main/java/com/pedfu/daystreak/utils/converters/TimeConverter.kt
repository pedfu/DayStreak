package com.pedfu.daystreak.utils.converters

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        val datetime = date ?: return null
        return datetime.time
    }

    @TypeConverter
    fun toDate(time: Long?): Date? {
        val timeLong = time ?: return null
        return Date(timeLong)
    }
}