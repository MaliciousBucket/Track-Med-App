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

fun DoseEntity.asLastTaken() = LastTakenDose(
    doseId= doseId,
    medicationId = medicationId,
    createdTime = createdTime,
    dosage = dosage
)

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


//sealed class DoseStatuses(val id: Int, @StringRes val stringValue: Int) {
//    object Taken : DoseStatuses(1, R.string.taken)
//    object Missed : DoseStatuses(2, R.string.missed)
//    object Skipped : DoseStatuses(3, R.string.skipped)
//    object Rescheduled : DoseStatuses(4, R.string.rescheduled)
//
//    companion object {
//        private val map = values().associateBy(DoseStatuses::id)
//
//        fun fromId(type: Int): DoseStatuses? {
//            return map[type]
//        }
//
//        private fun values() = listOf(Taken, Missed, Skipped, Rescheduled)
//    }
//}
