package com.example.newtrackmed.ui.feature.home

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.newtrackmed.data.Result
import com.example.newtrackmed.data.asResult
import com.example.newtrackmed.data.entity.DoseEntity
import com.example.newtrackmed.data.entity.FrequencyEntity
import com.example.newtrackmed.data.entity.FrequencyType
import com.example.newtrackmed.data.entity.MedicationEntity
import com.example.newtrackmed.data.model.DoseViewData
import com.example.newtrackmed.data.model.Medication
import com.example.newtrackmed.data.model.UpdateDoseData
import com.example.newtrackmed.data.model.asAsNeededDisplayDoseViewData
import com.example.newtrackmed.data.model.asDisplayModel
import com.example.newtrackmed.data.model.mapDoseStatusToChipStatus
import com.example.newtrackmed.data.repository.DoseRepository
import com.example.newtrackmed.data.repository.FrequencyRepository
import com.example.newtrackmed.data.repository.MedicationDoseCompositeRepository
import com.example.newtrackmed.data.repository.MedicationRepository
import com.example.newtrackmed.di.TrackMedApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.flow.*

data class TestUIState(
    val viewData:DoseDisplayUIState,
    val dateUiState: SelectedDateUIState
)


data class HomeUiState(
    val displayedDoses: DoseDisplayUIState,
    val updateDoseDialog: UpdateDoseDialogUIState,
    val selectedDate: SelectedDateUIState,
    val isError: Boolean,
)

@Immutable
sealed interface DoseDisplayUIState {
    data class Success(val doseViewData: List<DoseViewData>) : DoseDisplayUIState
    object Loading: DoseDisplayUIState
    object Error: DoseDisplayUIState
}

@Immutable
sealed interface UpdateDoseDialogUIState {
    data class Success(val updateDoseData: UpdateDoseData): UpdateDoseDialogUIState
    object Loading: UpdateDoseDialogUIState
    object Error: UpdateDoseDialogUIState
}

@Immutable
sealed interface SelectedDateUIState{
    data class Success(
        val selectedDate: LocalDateTime
    ): SelectedDateUIState
//    data class Loading(val displayText: String,)

}



class HomeScreenViewModel(
   private val compositeRepository: MedicationDoseCompositeRepository,
    private val frequencyRepository: FrequencyRepository,
    private val medicationRepository: MedicationRepository,
    private val doseRepository: DoseRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDateTime.now())

    val selectedDate = _selectedDate.asStateFlow()

//    private val _allMedications = MutableStateFlow<List<Medication>>(emptyList())

    private val _allMedications: Flow<List<MedicationEntity>> =
        medicationRepository.getAllMedications()

//    private val _activeMeds = MutableStateFlow<List<Medication>>(emptyList())

    private val _activeMeds: Flow<List<MedicationEntity>> =
        medicationRepository.getAllActiveMedications()


    private val _medicationIdsForDate = MutableStateFlow(setOf<Int>())

//    private val _allFrequencies = MutableStateFlow<List<FrequencyEntity>>(emptyList())

    private val _allFrequencies: Flow<List<FrequencyEntity>> =
        frequencyRepository.getAllFrequencies()


    private val filteredMedications: Flow<List<Medication>> = combine(
        _allMedications,
        _selectedDate,
        _allFrequencies
    ) { medications, selectedDate, frequencies ->
        val medsAndFreqs = medications.map { medication ->
            medication to frequencies.find { it.medicationId == medication.id }
        }

        // Apply filtering logic
        medsAndFreqs.filter { (medication, frequency) ->
            if (frequency == null) {
                return@filter false
            }
            if (frequency.asNeeded) {
                return@filter true
            }
            val interval = frequency.frequencyIntervals
            val daysBetween = ChronoUnit.DAYS.between(medication.startDate, selectedDate)
            val selectedDayOfWeek = selectedDate.dayOfWeek.value
            val selectedDayOfMonth = selectedDate.dayOfMonth
            when (frequency.frequencyType) {
                FrequencyType.DAILY -> {
                    return@filter true
                }

                FrequencyType.EVERY_OTHER -> {
                    return@filter daysBetween % 2 == 0L
                }

                FrequencyType.EVERY_X_DAYS -> {
                    return@filter daysBetween % frequency.frequencyIntervals[0] == 0L
                }

                FrequencyType.WEEK_DAYS -> {
                    return@filter frequency.frequencyIntervals.contains(selectedDayOfWeek)
                }

                FrequencyType.MONTH_DAYS -> {
                    return@filter frequency.frequencyIntervals.contains(selectedDayOfMonth)
                }
            }

            true
        }.mapNotNull { (medication, _) -> medication }
    }.distinctUntilChanged()
        .map { medicationEntityList ->
            medicationEntityList.map { it.asDisplayModel() }
        }


    val testDosesForDate: Flow<List<DoseEntity>> =
        _selectedDate.flatMapLatest { selectedDate ->
            doseRepository.getDosesForSelectedDate(selectedDate)
        }.distinctUntilChanged()





//TODO: PLACRHOLDER OLD ONE
//    private val doseViewDataList: Flow<List<DoseViewData>> = flow {
//        coroutineScope {
//            val innerFlow = combine(
//                filteredMedications,
//                testDosesForDate,
//                _allFrequencies
//            ) { filteredMeds, dosesForDate, allFrequencies ->
//                filteredMeds.flatMap { medication ->
//                    val correspondingDoses = dosesForDate.filter { it.medicationId == medication.id }
//                    val correspondingFrequency = allFrequencies.find { it.medicationId == medication.id }
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
//
//                    innerList
//                }
//            }
//            innerFlow.collect { viewDataList ->
//                emit(viewDataList)
//            }
//        }
//    }





//    private val doseViewDataListResult: Flow<Result<List<DoseViewData>>> =
//        createDoseViewDataListFlow(
//            filteredMedications,
//            testDosesForDate,
//            _allFrequencies,
//
//        ).asResult()












//    fun createDoseViewDataList(
//        filteredMedications: List<Medication>,
//        testDosesForDate: List<DoseEntity>,
//        allFrequencies: List<FrequencyEntity>
//    ): List<DoseViewData> {
//
//        return filteredMedications.flatMap { medication ->
//            val correspondingDoses = testDosesForDate.filter { it.medicationId == medication.id }
//            val correspondingFrequency = allFrequencies.find { it.medicationId == medication.id }
//            val innerList = mutableListOf<DoseViewData>()
//
//            if (correspondingFrequency?.asNeeded == true) {
//                innerList.add(medication.asAsNeededDisplayDoseViewData())
//            }
//
//            innerList.addAll(correspondingDoses.map { dose ->
//                DoseViewData(
//                    medicationId = medication.id,
//                    doseId = dose.doseId,
//                    name = medication.name,
//                    type = medication.type,
//                    dosage = medication.dosage,
//                    dosageUnit = medication.dosageUnit,
//                    unitsTaken = medication.unitsTaken,
//                    doseTime = dose.createdTime.toLocalTime(),
//                    chipStatus = mapDoseStatusToChipStatus(dose.status)
//                )
//            })
//            innerList
//        }
//    }

    fun createDoseViewDataList(
        filteredMedications: List<Medication>,
        testDosesForDate: List<DoseEntity>,
        allFrequencies: List<FrequencyEntity>
    ): Result<List<DoseViewData>> {
        return try {
            val result = filteredMedications.flatMap { medication ->
                val correspondingDoses = testDosesForDate.filter { it.medicationId == medication.id }
                val correspondingFrequency = allFrequencies.find { it.medicationId == medication.id }
                val innerList = mutableListOf<DoseViewData>()

                if (correspondingFrequency?.asNeeded == true) {
                    innerList.add(medication.asAsNeededDisplayDoseViewData())
                }

                innerList.addAll(correspondingDoses.map { dose ->
                    DoseViewData(
                        medicationId = medication.id,
                        doseId = dose.doseId,
                        name = medication.name,
                        type = medication.type,
                        dosage = medication.dosage,
                        dosageUnit = medication.dosageUnit,
                        unitsTaken = medication.unitsTaken,
                        doseTime = dose.createdTime.toLocalTime(),
                        chipStatus = mapDoseStatusToChipStatus(dose.status)
                    )
                })
                innerList
            }
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }


    val testMeds = MutableStateFlow<List<Medication>>(emptyList())
    val exampleFrequencis = MutableStateFlow<List<FrequencyEntity>>(emptyList())
    val exampleDoses = MutableStateFlow<List<DoseEntity>>(emptyList())

    val testMedsList: List<Medication> = emptyList()
    val exampleFrequenciesList: List<FrequencyEntity> = emptyList()
    val exampleDosesList: List<DoseEntity> = emptyList()

    //TODO: Very CLose

//    private val doseViewDataList: Flow<Result<List<DoseViewData>>> = flow {
//        try {
//            combine(
//                filteredMedications,
//                testDosesForDate,
//                _allFrequencies
//            ) { filteredMeds, dosesForDate, allFrequencies ->
//                filteredMeds.flatMap { medication ->
//                    val correspondingDoses = dosesForDate.filter { it.medicationId == medication.id }
//                    val correspondingFrequency = allFrequencies.find { it.medicationId == medication.id }
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
//
//                    innerList
//                }
//            }.collect { viewDataList ->
//                emit(Result.Success(viewDataList))
//            }
//        } catch (e: Exception) {
//            emit(Result.Error(e))
//        }
//    }.flowOn(Dispatchers.IO)

    private val doseViewDataList: Flow<List<DoseViewData>> = flow {
        try {
            combine(
                filteredMedications,
                testDosesForDate,
                _allFrequencies
            ) { filteredMeds, dosesForDate, allFrequencies ->
                filteredMeds.flatMap { medication ->
                    val correspondingDoses = dosesForDate.filter { it.medicationId == medication.id }
                    val correspondingFrequency = allFrequencies.find { it.medicationId == medication.id }
                    val innerList = mutableListOf<DoseViewData>()

                    if (correspondingFrequency?.asNeeded == true) {
                        innerList.add(medication.asAsNeededDisplayDoseViewData())
                    }

                    innerList.addAll(correspondingDoses.map { dose ->
                        DoseViewData(
                            medicationId = medication.id,
                            doseId = dose.doseId,
                            name = medication.name,
                            type = medication.type,
                            dosage = medication.dosage,
                            dosageUnit = medication.dosageUnit,
                            unitsTaken = medication.unitsTaken,
                            doseTime = dose.createdTime.toLocalTime(),
                            chipStatus = mapDoseStatusToChipStatus(dose.status)
                        )
                    })

                    innerList
                }
            }.collect { viewDataList ->
                Log.d("Debug View Data", "$viewDataList")
                emit(viewDataList)
            }
        } catch (e: Exception) {
            // Log or handle the error as you see fit
        }
    }.flowOn(Dispatchers.IO)
        .onEach {
            Log.d("Debug Get Data", "We getting data")
        }




    private val doseViewDataListTEST: Flow<Result<List<DoseViewData>>> = doseViewDataList.asResult()


    val uiState: StateFlow<TestUIState> = combine(
        doseViewDataListTEST,
       // doseViewDataList,
        _selectedDate
    ){ doseDataResult, currentData ->

//        val doseResult: DoseDisplayUIState = when (doseDataResult){
//                val (data) = doseDataResult
//            Result.Success(doseDataResult) -> DoseDisplayUIState.Success(
//            Result.Loading -> DoseDisplayUIState.Loading
//            Result.Error() -> DoseDisplayUIState.Error
//            com.example.newtrackmed.data.Result.Success(doseDataResult) -> DoseDisplayUIState.Success(doseDataResult)
////            Result.isError
//        }
        val doseResult: DoseDisplayUIState = when (doseDataResult) {
           is Result.Success -> {
               val (data) = doseDataResult
               DoseDisplayUIState.Success(data)
            }
            is Result.Loading -> DoseDisplayUIState.Loading
            is Result.Error -> DoseDisplayUIState.Error

       }

        val date: SelectedDateUIState = SelectedDateUIState.Success(_selectedDate.value)

        TestUIState(
            doseResult,
            date
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = TestUIState(
            DoseDisplayUIState.Loading,
            SelectedDateUIState.Success(LocalDateTime.now())
        )
    )

    fun onNextDateClicked() {
        Log.d("Next Date Clicked", "Before Change: ${_selectedDate.value}")
        val newDate = _selectedDate.value.plusDays(1)
        _selectedDate.update { it.plusDays(1) }
        Log.d("Next Date Clicked", "After Change: ${_selectedDate.value}")

    }

    fun onPreviousDateClicked() {
        Log.d("Next Date Clicked", "Before Change: ${_selectedDate.value}")
        val newDate = _selectedDate.value.plusDays(1)
        _selectedDate.update {newDate
        }
        Log.d("Next Date Clicked", "After Change: ${_selectedDate.value}")

    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = SavedStateHandle()

                val appModule = TrackMedApp.appModule
                val medicationRepository = appModule.medicationRepository
                val frequencyRepository = appModule.frequencyRepository
                val doseRepository = appModule.doseRepository
                val compositeRepository = appModule.compositeRepository

                HomeScreenViewModel(
                    compositeRepository = compositeRepository,
                    medicationRepository = medicationRepository,
                    frequencyRepository = frequencyRepository,
                    doseRepository = doseRepository,
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }


}


