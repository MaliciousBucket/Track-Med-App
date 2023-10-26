package com.example.newtrackmed.data.model

import com.example.newtrackmed.data.entity.DoseRescheduleHistory
import com.example.newtrackmed.data.entity.MedicationEntity
import java.time.LocalDateTime

data class RecentDoseDetails internal constructor(
    val name: String,
    val doseId: Int,
    val dosage: Int,
    val type: String,
    val doseTime: LocalDateTime,
    val histories: List<DoseRescheduleHistory>?,
    val chipStatus: DoseChipStatus
) {
    constructor(dose: DoseWithHistory, medication: MedicationEntity): this (
        name = medication.name,
        doseId = dose.doseEntity.doseId,
        dosage = dose.doseEntity.dosage,
        type = medication.type,
        doseTime = dose.doseEntity.createdTime,
        histories = dose.histories,
        chipStatus = mapDoseStatusToChipStatus(dose.doseEntity.status)
    )
}

fun List<DoseWithHistory>.mapToRecentDoseDetails(medication: MedicationEntity): List<RecentDoseDetails> {
    return map { RecentDoseDetails(it, medication) }
}


