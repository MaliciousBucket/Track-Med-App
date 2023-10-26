package com.example.newtrackmed.util

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {

    @TypeConverter
    fun toDate(timestamp: String?): LocalDate? {
        return timestamp?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun toTimestamp(date: LocalDate?): String? {
        return date?.toString()
    }
}