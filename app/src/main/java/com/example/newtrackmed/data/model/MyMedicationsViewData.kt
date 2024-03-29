package com.example.newtrackmed.data.model

import com.example.newtrackmed.data.entity.MedicationEntity
import java.time.LocalTime

data class MyMedicationsViewData internal constructor(
    val medicationId : Int,
    val isActive: Boolean,
    val name: String,
    val type: String,
    val dosage: Int,
    val dosageUnit: String,
    val unitsTaken: Int,
    val timeToTake: LocalTime,
    val lastTakenDose: LastTakenDose?
){
    constructor(medication: Medication, lastTakenDose: LastTakenDose?) : this (
        medicationId = medication.id,
        isActive = true,
        name = medication.name,
        type = medication.type,
        dosage = medication.dosage,
        dosageUnit = medication.dosageUnit,
        unitsTaken = medication.unitsTaken,
        timeToTake = medication.timeToTake,
        lastTakenDose = lastTakenDose
    )
}

fun List<Pair<Medication, LastTakenDose?>>.mapToMyMedicationsViewData(): List<MyMedicationsViewData>{
    return map {(medication, lastDose) ->
        MyMedicationsViewData(medication, lastDose)
    }
}

fun MedicationEntity.mapToMyMedicationsViewData(lastDose: LastTakenDose?) = MyMedicationsViewData(
    medicationId = id,
    isActive = isActive,
    name = name,
    type = type,
    dosage = dosage,
    dosageUnit = dosageUnit,
    unitsTaken = unitsTaken,
    timeToTake = timeToTake,
    lastTakenDose = lastDose
)
