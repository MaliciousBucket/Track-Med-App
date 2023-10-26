package com.example.newtrackmed.util

import androidx.room.TypeConverter
import com.example.newtrackmed.data.entity.FrequencyType

class FrequencyTypeConverter {

    @TypeConverter
    fun toFrequencyType(value: Int) = enumValues<FrequencyType>()[value]

    @TypeConverter
    fun fromFrequencyType(value: FrequencyType) = value.ordinal

//    @TypeConverter
//    fun toFrequencyType(value: String) = enumValues<FrequencyType>()(value)
}