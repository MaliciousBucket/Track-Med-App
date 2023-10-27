package com.example.newtrackmed.data.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalTime
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import com.example.newtrackmed.R
import com.example.newtrackmed.data.entity.DoseEntity
import com.example.newtrackmed.data.entity.DoseStatus
import java.time.LocalDateTime



data class DoseViewData internal constructor(
    val medicationId : Int,
    val doseId: Int?,
    val name: String,
    val type: String,
    val dosage: Int,
    val dosageUnit: String,
    val unitsTaken: Int,
    val doseTime: LocalTime,
    val chipStatus: DoseChipStatus
){
    constructor(medication: Medication, dose: DoseEntity?, selectedDate: LocalDateTime) : this (
        medicationId = medication.id,
        doseId = dose?.doseId,
        name = medication.name,
        type = medication.type,
        dosage = medication.dosage,
        dosageUnit = medication.dosageUnit,
        unitsTaken = medication.unitsTaken,
        doseTime = dose?.createdTime?.toLocalTime() ?: medication.timeToTake,
        chipStatus = if(dose != null){
            mapDoseStatusToChipStatus(dose.status)
        } else {
            mapMedicationToDoseStatusChip(medication.timeToTake, selectedDate)
        }
    )

}
//TODO: Check if its possible to avoid the list and just map to singles
fun List<Pair<Medication, DoseEntity?>>.mapToDoseViewData(selectedDate: LocalDateTime): List<DoseViewData> {
    return map { (medication, dose) ->
        DoseViewData(medication, dose, selectedDate)
    }
}



fun Medication.asAsNeededDisplayDoseViewData() = DoseViewData(
    medicationId = id,
    doseId = null,
    name = name,
    type = type,
    dosage = dosage,
    dosageUnit = dosageUnit,
    unitsTaken = unitsTaken,
    doseTime = timeToTake,
    chipStatus = DoseChipStatus.Upcoming

)

fun mapMedicationToDoseStatusChip(timeToTake: LocalTime, selectedDate: LocalDateTime) : DoseChipStatus {
    val currentTime = LocalDateTime.now()
    return when {
        selectedDate.toLocalDate().isBefore(currentTime.toLocalDate()) -> {
            DoseChipStatus.Missing
        }

        selectedDate.toLocalDate().isAfter(currentTime.toLocalDate()) -> {
            DoseChipStatus.FutureDose
        }

        selectedDate.toLocalDate().isEqual(currentTime.toLocalDate())
                && selectedDate.isBefore(currentTime)-> {
            DoseChipStatus.Missing
        }

        else -> {
            DoseChipStatus.Upcoming
        }
    }
}


fun mapDoseStatusToChipStatus(doseStatus: DoseStatus): DoseChipStatus {
    return when(doseStatus) {
        DoseStatus.TAKEN -> {
            DoseChipStatus.Taken
        }
        DoseStatus.MISSED -> {
            DoseChipStatus.Missed
        }

        DoseStatus.SKIPPED -> {
            DoseChipStatus.Skipped
        }

        DoseStatus.RESCHEDULED -> {
            DoseChipStatus.Rescheduled
        }
    }
}


sealed class DoseChipStatus(
    @StringRes val stringValue: Int,
    val icon: ImageVector,
    val chipColor: Color,
    val chipLabelColor: Color
){
    object Taken: DoseChipStatus(
        R.string.taken,
        Icons.Filled.Check,
        Color.Green,
        Color.White
    )
    object Missed: DoseChipStatus(
        R.string.missed,
        Icons.Filled.Close,
        Color.Red,
        Color.White
    )
    object Skipped: DoseChipStatus(
        R.string.skipped,
        Icons.AutoMirrored.Filled.Redo,
        Color.Yellow,
        Color.Black
    )
    object Rescheduled: DoseChipStatus(
        R.string.rescheduled,
        Icons.Filled.Timer,
        Color.Blue,
        Color.White
    )
    object Upcoming: DoseChipStatus(
        R.string.take,
        Icons.Filled.Add,
        Color.Cyan,
        Color.Black
    )
    object Missing : DoseChipStatus(
        R.string.take,
        Icons.Filled.PriorityHigh,
        Color.hsv(
            hue = 30f,
            saturation = 1f,
            value = 1f,
            colorSpace = ColorSpaces.Srgb
        ),
        Color.Black
    )
    object FutureDose: DoseChipStatus(
        R.string.take,
        Icons.Filled.Add,
        Color.Gray,
        Color.White
    )
}



