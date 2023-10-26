package com.example.newtrackmed.data.model

import com.example.newtrackmed.data.entity.MedicationEntity
import java.time.LocalTime

data class Medication (
    val id: Int,
    val name: String,
    val type: String,
    val dosage: Int,
    val dosageUnit: String,
    val unitsTaken: Int,
    val timeToTake: LocalTime,
)

fun MedicationEntity.asDisplayModel() = Medication(
    id = id,
    name = name,
    type = type,
    dosage = dosage,
    dosageUnit = dosageUnit,
    unitsTaken = unitsTaken,
    timeToTake = timeToTake
)
