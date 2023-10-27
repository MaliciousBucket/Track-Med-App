package com.example.newtrackmed.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity= MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["medicationId"])]
)
data class FrequencyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val medicationId: Int,
    val frequencyIntervals: List<Int>,
    val frequencyType: FrequencyType,
    val asNeeded: Boolean
)

enum class FrequencyType {
    DAILY,
    EVERY_OTHER,
    EVERY_X_DAYS,
    WEEK_DAYS,
    MONTH_DAYS
}
