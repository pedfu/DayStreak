package com.pedfu.daystreak.data.remote.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Date

object DateAdapter {
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy")

    @FromJson
    fun fromJson(dateString: String): Date {
        return dateFormat.parse(dateString) ?: throw IllegalArgumentException("Invalid date format: $dateString")
    }

    @ToJson
    fun toJson(date: Date): String {
        return dateFormat.format(date)
    }
}
