package com.example.newtrackmed.util

import androidx.room.TypeConverter

class IntListConverter {
    @TypeConverter
    fun toIntList(value: String) = value.split(",").map { it.toInt() }

    @TypeConverter
    fun fromIntList(list: List<Int>) = list.joinToString(",")
}