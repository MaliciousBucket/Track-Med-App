package com.example.newtrackmed.util

import androidx.room.TypeConverter
import java.time.LocalDateTime

class LocalDateTimeConverter {

    @TypeConverter
    fun toDate(timestamp: String?): LocalDateTime? {
        return timestamp?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun toTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}