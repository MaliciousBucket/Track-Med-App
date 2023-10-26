package com.example.newtrackmed.data.repository

import com.example.newtrackmed.data.entity.FrequencyEntity
import com.example.newtrackmed.data.entity.MedicationEntity
import com.example.newtrackmed.data.model.DoseViewData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class MedicationDoseCompositeRepository(
    private val doseRepository: DoseRepository,
    private val medicationRepository: MedicationRepository
) {
    private val allFrequencies: List<FrequencyEntity> = emptyList()

    private val allMedications: List<MedicationEntity> = emptyList()

    private val medicationIdsForDate = MutableStateFlow<Set<Int>>(setOf())

    private val selectedDate = MutableStateFlow<LocalDateTime>(LocalDateTime.now())

    fun getDoseViewDataForSelectedDate(selectedDate: LocalDateTime): Flow<List<DoseViewData>> {
        return doseRepository.getDosesForSelectedDate(selectedDate).map {

        }
    }

    fun getUpdateDoseDataForDate(){

    }

    private fun filterMedicationsForDate(selectedDate: LocalDateTime){
        val selectedLocalDate = selectedDate.toLocalDate()


    }



}