package com.example.newtrackmed.util

import androidx.room.TypeConverter
import com.example.newtrackmed.data.entity.DoseStatus

class DoseStatusConverter {

    @TypeConverter
    fun toDoseStatus(value: Int) = enumValues<DoseStatus>()[value]

    @TypeConverter
    fun fromDoseStatus(value: DoseStatus) = value.ordinal
}