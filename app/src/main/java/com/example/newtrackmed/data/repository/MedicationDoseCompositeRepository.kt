package com.example.newtrackmed.data.repository

import com.example.newtrackmed.data.entity.DoseEntity
import com.example.newtrackmed.data.entity.FrequencyEntity
import com.example.newtrackmed.data.entity.MedicationEntity
import com.example.newtrackmed.data.model.DoseViewData
import com.example.newtrackmed.data.model.DoseWithHistory
import com.example.newtrackmed.data.model.LastTakenDose
import com.example.newtrackmed.data.model.Medication
import com.example.newtrackmed.data.model.UpdateDoseData
import com.example.newtrackmed.data.model.asAsNeededDisplayDoseViewData
import com.example.newtrackmed.data.model.asDisplayModel
import com.example.newtrackmed.data.model.mapDoseStatusToChipStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class DoseFeed(
    val asNeededViewData: List<DoseViewData>,
    val dosesForDateViewData: List<DoseViewData>,
    val scheduledMedsViewData: List<DoseViewData>,
) {
    val allData: List<DoseViewData> =
        asNeededViewData + dosesForDateViewData + scheduledMedsViewData
}


class MedicationDoseCompositeRepository(
    private val doseRepository: DoseRepository,
    private val medicationRepository: MedicationRepository,
    private val frequencyRepository: FrequencyRepository,
//    private val coroutineScope: CoroutineScope
) {
//    private val allFrequencies: List<FrequencyEntity> = emptyList()
//
//    private val allMedications: List<MedicationEntity> = emptyList()
//
//    private val medicationIdsForDate = MutableStateFlow<Set<Int>>(setOf())
//
//    private val selectedDate = MutableStateFlow<LocalDateTime>(LocalDateTime.now())
//
//    private val allActiveMedciations: Flow<List<MedicationEntity>> =
//        medicationRepository.getAllActiveMedications()

//    fun getDoseViewDataForSelectedDate(selectedDate: LocalDateTime): Flow<List<DoseViewData>> {
//        return doseRepository.getDosesForSelectedDate(selectedDate).map {
//
//        }
//    }

    suspend fun getAllFrequencies(){

    }

    suspend fun getAllMedications(){

    }

    suspend fun getAllActiveMedications(){

    }

    suspend fun getAllDosesForDate(){

    }

    private val viewData = MutableStateFlow<DoseViewData?>(null)

    fun observeDoseFeed(): Flow<DoseViewData?> = viewData



    suspend

    fun getUpdateDoseData(medicationId: Int, doseId: Int?): Flow<UpdateDoseData> {
        if(doseId == null) {
            val lastDoseFlow: Flow<LastTakenDose> =
                doseRepository.getLastTakenDoseForMed(medicationId)
            val medicationFlow: Flow<MedicationEntity> =
                medicationRepository.getMedicationById(medicationId)


            return combine(medicationFlow, lastDoseFlow) { medication, lastDose ->
                UpdateDoseData(
                    medication = medication,
                    dose = null,
                    lastTakenDose = lastDose
                )
            }
        }
        val lastDoseFlow: Flow<LastTakenDose> = doseRepository.getLastTakenDoseForMed(medicationId)
        val medicationFlow: Flow<MedicationEntity> = medicationRepository.getMedicationById(medicationId)
        val doseWithHistoryFlow: Flow<DoseWithHistory> = doseRepository.getDoseWithHistoryById(doseId)

        return combine(medicationFlow, lastDoseFlow, doseWithHistoryFlow) { medication, lastDose, doseWithHistory ->
            UpdateDoseData(
                medication = medication,
                dose = doseWithHistory,
                lastTakenDose = lastDose
            )
        }

    }


//    fun getUpdateDoseDataForMedAndDose(medicationId: Int, doseId: Int): Flow<UpdateDoseData> {
//        val lastDoseFlow: Flow<LastTakenDose> = doseRepository.getLastTakenDoseForMed(medicationId)
//        val medicationFlow: Flow<MedicationEntity> = medicationRepository.getMedicationById(medicationId)
//        val doseWithHistoryFlow: Flow<DoseWithHistory> = doseRepository.getDoseWithHistoryById(doseId)
//
//        return combine(medicationFlow, lastDoseFlow, doseWithHistoryFlow) { medication, lastDose, doseWithHistory ->
//            UpdateDoseData(
//                medication = medication,
//                dose = doseWithHistory,
//                lastTakenDose = lastDose
//            )
//        }
//    }


    ----- OLD, NEED TO CHECK -----

    private fun filterMedicationsForDate(selectedDate: LocalDateTime){
        val selectedLocalDate = selectedDate.toLocalDate()


    }

    private val _allMedications: Flow<List<MedicationEntity>> =
        medicationRepository.getAllMedications()

    private val _activeMeds: Flow<List<MedicationEntity>> =
        medicationRepository.getAllActiveMedications()

    private val _allFrequencies: Flow<List<FrequencyEntity>> =
        frequencyRepository.getAllFrequencies()



//    fun createDoseViewDataListFlow(
//        filteredMedications: Flow<List<Medication>>,
//        testDosesForDate: Flow<List<DoseEntity>>,
//        allFrequencies: Flow<List<FrequencyEntity>>
//    ): StateFlow<List<DoseViewData>> {
//        val mutableStateFlow = MutableStateFlow<List<DoseViewData>>(emptyList())
//
//
//        coroutineScope.launch {
//            combine(
//                filteredMedications,
//                testDosesForDate,
//                allFrequencies
//            ) { filteredMeds, dosesForDate, allFreqs ->
//                // Your existing logic here
//                filteredMeds.flatMap { medication ->
//                    val correspondingDoses = dosesForDate.filter { it.medicationId == medication.id }
//                    val correspondingFrequency = allFreqs.find { it.medicationId == medication.id }
//                    val innerList = mutableListOf<DoseViewData>()
//
//                    if (correspondingFrequency?.asNeeded == true) {
//                        innerList.add(medication.asAsNeededDisplayDoseViewData())
//                    }
//
//                    innerList.addAll(correspondingDoses.map { dose ->
//                        DoseViewData(
//                            medicationId = medication.id,
//                            doseId = dose.doseId,
//                            name = medication.name,
//                            type = medication.type,
//                            dosage = medication.dosage,
//                            dosageUnit = medication.dosageUnit,
//                            unitsTaken = medication.unitsTaken,
//                            doseTime = dose.createdTime.toLocalTime(),
//                            chipStatus = mapDoseStatusToChipStatus(dose.status)
//                        )
//                    })
//                    innerList
//                }
//            }.flowOn(Dispatchers.IO).collect { combinedList ->
//                mutableStateFlow.value = combinedList
//            }
//        }
//
//        return mutableStateFlow.asStateFlow()
//    }







}