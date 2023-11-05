package com.example.newtrackmed.data

import com.example.newtrackmed.data.entity.DoseEntity
import com.example.newtrackmed.data.entity.DoseStatus
import com.example.newtrackmed.data.entity.MedicationEntity
import com.example.newtrackmed.data.entity.asLastTaken
import com.example.newtrackmed.data.model.Medication
import com.example.newtrackmed.data.model.UpdateDoseActions
import com.example.newtrackmed.data.model.UpdateDoseData
import com.example.newtrackmed.data.model.asDisplayModel
import com.example.newtrackmed.data.model.mapToDoseViewData
import com.example.newtrackmed.data.model.mapToDoseWithHistory
import com.example.newtrackmed.data.model.mapToMyMedicationsViewData
import com.example.newtrackmed.data.model.mapToRecentDoseDetails
import com.example.newtrackmed.data.model.mapToUpdateDoseData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime



val previewMedicationEntities = listOf(
    MedicationEntity(
        id = 1,
        name = "Ibuprofen",
        type = "Pill",
        dosage = 200,
        dosageUnit = "mg",
        unitsTaken = 1,
        timeToTake = LocalTime.of(8, 0),
        instructions = "Take with food",
        notes = "Check for allergies",
        startDate = LocalDate.now(),
        endDate = LocalDate.now().plusDays(90),
        isActive = true,
        isDeleted = false
    ),
    MedicationEntity(
        id = 2,
        name = "Paracetamol",
        type = "Sachet",
        dosage = 500,
        dosageUnit = "mg",
        unitsTaken = 1,
        timeToTake = LocalTime.of(12, 30),
        instructions = "Take on an empty stomach",
        notes = "Not for kids below 12",
        startDate = LocalDate.now(),
        endDate = LocalDate.now().plusDays(90),
        isActive = true,
        isDeleted = false
    ),
    MedicationEntity(
        id = 3,
        name = "Aspirin",
        type = "Pill",
        dosage = 100,
        dosageUnit = "mg",
        unitsTaken = 2,
        timeToTake = LocalTime.of(18, 0),
        instructions = "Take with water",
        notes = null,
        startDate = LocalDate.now(),
        endDate = LocalDate.now().plusDays(90),
        isActive = true,
        isDeleted = false
    )
)

// Create preview doses
val previewDoses = listOf(
    DoseEntity(
        doseId = 1,
        medicationId = 1,
        status = DoseStatus.TAKEN,
        dosage = 200,
        createdTime = LocalDateTime.now()
    ),
    DoseEntity(
        doseId = 2,
        medicationId = 1,
        status = DoseStatus.MISSED,
        dosage = 200,
        createdTime = LocalDateTime.now().plusDays(1)
    ),
    DoseEntity(
        doseId = 3,
        medicationId = 2,
        status = DoseStatus.TAKEN,
        dosage = 500,
        createdTime = LocalDateTime.now()
    ),
    DoseEntity(
        doseId = 4,
        medicationId = 2,
        status = DoseStatus.SKIPPED,
        dosage = 500,
        createdTime = LocalDateTime.now().plusDays(1)
    ),
    DoseEntity(
        doseId = 5,
        medicationId = 3,
        status = DoseStatus.TAKEN,
        dosage = 100,
        createdTime = LocalDateTime.now()
    ),
    DoseEntity(
        doseId = 6,
        medicationId = 3,
        status = DoseStatus.MISSED,
        dosage = 100,
        createdTime = LocalDateTime.now().plusDays(1)
    )
)

val previewMedications = listOf(
    previewMedicationEntities[0].asDisplayModel(),
    previewMedicationEntities[1].asDisplayModel(),
    previewMedicationEntities[2].asDisplayModel()
)

val previewDoseViewData= listOf(
    Pair(previewMedications[0], previewDoses[0]),
    Pair(previewMedications[1], previewDoses[1]),
    Pair(previewMedications[2], previewDoses[2]),



)

val doses = listOf(
    DoseEntity(
        doseId = 1,
        medicationId = 1,
        status = DoseStatus.TAKEN,
        dosage = 200,
        createdTime = LocalDateTime.now()
    ),
    DoseEntity(
        doseId = 2,
        medicationId = 1,
        status = DoseStatus.MISSED,
        dosage = 200,
        createdTime = LocalDateTime.now().plusDays(1)
    )
)

val medicationDosePairs = listOf(
    Pair(previewMedications[0], doses[0]),
    Pair(previewMedications[0], doses[1]),
    Pair(previewMedications[1], null)
)

val selectedDate = LocalDateTime.now()

val doseViewDataList = medicationDosePairs.mapToDoseViewData(selectedDate)

val testUpdateDataData = listOf(
    Triple(
        previewMedicationEntities[1],
        previewDoses[1].mapToDoseWithHistory(),
        previewDoses[2].asLastTaken()
    )
)

val previewUpdateDoseData = testUpdateDataData.mapToUpdateDoseData(LocalDateTime.now())

val previewMyMedicationsLastDoses = listOf(
    previewDoses[0].asLastTaken(),
    previewDoses[1].asLastTaken(),
    previewDoses[2].asLastTaken()

)

val previewMyMedicationsData = listOf(
    Pair(previewMedications[0], previewMyMedicationsLastDoses[0]),
    Pair(previewMedications[1], previewMyMedicationsLastDoses[1]),
    Pair(previewMedications[2], previewMyMedicationsLastDoses[2])
)

val myMedicationsPreviewData = previewMyMedicationsData.mapToMyMedicationsViewData()

val recentDosePreviewData = listOf(
    previewDoses[0].mapToDoseWithHistory(),
    previewDoses[1].mapToDoseWithHistory(),
    previewDoses[2].mapToDoseWithHistory(),
    previewDoses[3].mapToDoseWithHistory(),
    previewDoses[4].mapToDoseWithHistory(),
    previewDoses[5].mapToDoseWithHistory(),
)

val recentDosePreviewList = recentDosePreviewData.mapToRecentDoseDetails(previewMedicationEntities[0])

val previewUpdateTestData = UpdateDoseData(
    medicationId = 1,
    doseId = null,
    name = "Mesalazine",
    type = "Sachet",
    dosage = 2,
    dosageUnit = "g",
    unitsTaken = 2,
    doseTime = LocalTime.of(9, 30),
    notes = "",
    instructions = "Take with water",
    updateDoseActions = UpdateDoseActions.FUTURE,
    lastTakenDose = null,
    rescheduleHistory = null
)