package com.example.newtrackmed.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val type: String,
    val dosage: Int,
    val dosageUnit: String,
    val unitsTaken: Int,
    val timeToTake: LocalTime,
    val instructions: String?,
    val notes: String?,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val isActive: Boolean,
    val isDeleted: Boolean
)