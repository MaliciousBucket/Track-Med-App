package com.example.newtrackmed.data.model

import com.example.newtrackmed.data.entity.DoseEntity
import com.example.newtrackmed.data.entity.DoseRescheduleHistory
import com.example.newtrackmed.data.entity.DoseStatus
import com.example.newtrackmed.data.entity.MedicationEntity
import java.time.LocalTime

data class UpdateDoseData internal constructor(
    val medicationId: Int,
    val doseId: Int?,
    val name: String,
    val type: String,
    val dosage: Int,
    val dosageUnit: String,
    val unitsTaken: Int,
    val doseTime: LocalTime,
    val notes: String?,
    val instructions: String?,
    val updateDoseActions: UpdateDoseActions,
    val lastTakenDose: LastTakenDose?,
    val rescheduleHistory: List<DoseRescheduleHistory>?


){
    constructor(medication: MedicationEntity,
                dose: DoseWithHistory?,
                lastTakenDose: LastTakenDose?
    ): this (
        medicationId = medication.id,
        doseId = dose?.doseEntity?.doseId,
        name = medication.name,
        type = medication.type,
        dosage = medication.dosage,
        dosageUnit = medication.dosageUnit,
        unitsTaken = medication.unitsTaken,
        doseTime = dose?.doseEntity?.createdTime?.toLocalTime() ?: medication.timeToTake,
        notes = medication.notes,
        instructions = medication.instructions,
        updateDoseActions = dose?.doseEntity?.status.toUpdateDoseActions(),
        lastTakenDose = lastTakenDose,
        rescheduleHistory = dose?.histories
    )
}


fun List<Triple<MedicationEntity, DoseWithHistory?, LastTakenDose?>>.mapToUpdateDoseData(): List<UpdateDoseData> {
    return map { (medication, dose, lastTakenDose) ->
        UpdateDoseData(medication, dose, lastTakenDose)
    }
}

//fun MedicationEntity.mapToUpdateDoseData() = UpdateDoseData (
//    medicationId = id,
//    doseId = null,
//    name = name,
//    type = type,
//    dosage = dosage,
//    dosageUnit = dosageUnit,
//    unitsTaken = unitsTaken,
//    doseTime = timeToTake,
//    notes = notes,
//    instructions = instructions,
//    updateDoseActions = UpdateDoseActions.TAKE,
//
//
//    )



enum class UpdateDoseActions{
    TAKEN,
    MISSED,
    SKIPPED,
    RESCHEDULED,
    TAKE,
    FUTURE
}

fun DoseStatus?.toUpdateDoseActions(): UpdateDoseActions {
    return when (this) {
        DoseStatus.TAKEN -> UpdateDoseActions.TAKEN
        DoseStatus.MISSED -> UpdateDoseActions.MISSED
        DoseStatus.SKIPPED -> UpdateDoseActions.SKIPPED
        DoseStatus.RESCHEDULED -> UpdateDoseActions.RESCHEDULED
        null -> UpdateDoseActions.TAKE
    }
}
