package com.example.newtrackmed.util

import androidx.room.TypeConverter
import java.time.LocalTime

class LocalTimeConverter {

    @TypeConverter
    fun toTime(timeString: String?): LocalTime? {
        return timeString?.let { LocalTime.parse(it) }
    }

    @TypeConverter
    fun fromTime(time: LocalTime?): String? {
        return time?.toString()
    }
}
