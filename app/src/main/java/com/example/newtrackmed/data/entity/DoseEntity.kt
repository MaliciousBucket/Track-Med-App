package com.example.newtrackmed.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.newtrackmed.data.model.LastTakenDose
import java.time.LocalDateTime


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
data class DoseEntity(
    @PrimaryKey(autoGenerate = true)
    val doseId: Int,
    val medicationId: Int,
    val status: DoseStatus,
    val dosage: Int,
    val createdTime: LocalDateTime,
)

enum class DoseStatus(){
    TAKEN,
    MISSED,
    SKIPPED,
    RESCHEDULED
}
//Mapping extension functions
fun DoseEntity.asLastTaken() = LastTakenDose(
    doseId= doseId,
    medicationId = medicationId,
    createdTime = createdTime,
    dosage = dosage
)
//For when a medication doesn't have a matching dose
fun MedicationEntity.mapToDoseEntity(
    status: DoseStatus,
    updateTime: LocalDateTime?,
    newDosage: Int?) = DoseEntity(
    doseId = 0,
    medicationId = id,
    status = status,
    dosage = newDosage ?: dosage,
    createdTime = updateTime ?: LocalDateTime.now()
)

