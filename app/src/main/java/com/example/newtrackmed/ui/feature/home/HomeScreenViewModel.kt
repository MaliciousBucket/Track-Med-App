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
import com.example.newtrackmed.data.entity.DoseStatus
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime

data class TestUIState(
    val viewData:DoseDisplayUIState,
    val dateUiState: SelectedDateUIState,
    val dialogUIState: UpdateDoseDialogUIState
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
    data class Success(val updateDoseData: UpdateDoseData?): UpdateDoseDialogUIState
    object Hidden: UpdateDoseDialogUIState
}

@Immutable
sealed interface SelectedDateUIState{
    data class Success(
        val selectedDate: LocalDateTime
    ): SelectedDateUIState
    object Loading: SelectedDateUIState
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


    fun refreshData() {

    }

    fun onDateChange(newDate: LocalDateTime) {

    }

    private val _flowMeds: Flow<Result<List<DoseViewData>>> =
        compositeRepository.testMeds().asResult()

    private val furtherTesting: Flow<Result<List<DoseViewData>>> = _selectedDate.map {it }
        .distinctUntilChanged().flatMapLatest { selectedDate ->
            compositeRepository.createTestData(selectedDate).asResult()
        }


//    private val furtherTesting: Flow<Result<List<DoseViewData>>> = _selectedDate.flatMapLatest { selectedDate ->
//        compositeRepository.createTestData(selectedDate).asResult()
//    }

    private val _showDialogState = MutableStateFlow<UpdateDoseDialogUIState>(UpdateDoseDialogUIState.Hidden)


    private val _updateDoseData = MutableStateFlow<UpdateDoseData?>(null)




    init {
        viewModelScope.launch {
            compositeRepository.setup()
        }

    }

    val uiState: StateFlow<TestUIState> = combine(
        furtherTesting,
        _selectedDate,
        _showDialogState
//        dialogState
    ) { doseDataResult, currentData, dialogState  ->
        val doseState: DoseDisplayUIState = when (doseDataResult) {
            is Result.Success -> DoseDisplayUIState.Success(doseDataResult.data)
            is Result.Loading -> DoseDisplayUIState.Loading
            is Result.Error -> DoseDisplayUIState.Error
            else -> {
                DoseDisplayUIState.Error
            }
        }

        val dateState: SelectedDateUIState = SelectedDateUIState.Success(LocalDateTime.now())

        val dialogState: UpdateDoseDialogUIState = when (dialogState) {
            is UpdateDoseDialogUIState.Success -> UpdateDoseDialogUIState.Success(_updateDoseData.value)
            is UpdateDoseDialogUIState.Hidden ->UpdateDoseDialogUIState.Hidden
        }

    TestUIState(
        doseState,
        dateState,
         dialogState
    )
}.stateIn(
    scope = viewModelScope,
//    started = SharingStarted.WhileSubscribed(5000),
        started= SharingStarted.Lazily,
    initialValue = TestUIState(
        DoseDisplayUIState.Loading,
        SelectedDateUIState.Success(LocalDateTime.now()),
        UpdateDoseDialogUIState.Hidden
    )
)
fun onCardClicked(medicationId: Int, doseId: Int?) {
    Log.d("Card Clicked", "Card: $medicationId")
    viewModelScope.launch {
        val updateData = compositeRepository.getUpdateDoseData(medicationId, doseId)
            .distinctUntilChanged()
            .first()
        _updateDoseData.update { updateData }
        _showDialogState.update { UpdateDoseDialogUIState.Success(_updateDoseData.value) }
    }
}


    fun onCancelDialogClicked(){
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                _showDialogState.update { UpdateDoseDialogUIState.Hidden }
            }
        }
    }

    fun onTakeClicked() {
//        viewModelScope.launch {

//        }
        //onCancelDialogClicked()
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                _showDialogState.update { UpdateDoseDialogUIState.Hidden }
                val updateData = _updateDoseData.value
                if (updateData != null) {
                    val selectedDate = _selectedDate.value
                    val updateTime = if (selectedDate.toLocalDate().isBefore(LocalDate.now())) {
                        selectedDate.toLocalDate().atTime(updateData?.doseTime)
                    } else {
                        LocalDateTime.now()
                    }
                    if (updateData.doseId != null) {
                        compositeRepository.updateDoseStatus(
                            updateData.doseId,
                            DoseStatus.TAKEN,
                            updateTime
                        )
                    } else {
                        compositeRepository.createDose(
                            updateData.medicationId,
                            DoseStatus.TAKEN,
                            null,
                            updateTime
                        )
                    }
                }
            }
        }
    }

    fun onUnTakeClicked(){
        viewModelScope.launch {
            _showDialogState.update { UpdateDoseDialogUIState.Hidden }
            if(_selectedDate.value.isBefore(LocalDateTime.now())){
                val missedDate = _selectedDate.value.toLocalDate()
                val missedDateTime = missedDate.atTime(_updateDoseData.value?.doseTime ?: LocalTime.now() )
                _updateDoseData.value?.doseId?.let {
                    Log.d("Debug UnTake", "MissedTime: $missedDateTime")
                    compositeRepository.updateDoseStatus(it, DoseStatus.MISSED, missedDateTime ) }
            } else {
                _updateDoseData.value?.doseId?.let { compositeRepository.deleteDose(it) }
            }
        }
    }


    fun onSkippedClicked() {
        viewModelScope.launch {
            _showDialogState.update { UpdateDoseDialogUIState.Hidden }
            val updateData = _updateDoseData.value
            if (updateData != null) {
                val selectedDate = _selectedDate.value
                val updateTime = if (selectedDate.toLocalDate().isBefore(LocalDate.now())) {
                    selectedDate.toLocalDate().atTime(updateData?.doseTime)
                } else {
                    LocalDateTime.now()
                }
                if (updateData.doseId != null) {
                    compositeRepository.updateDoseStatus(
                        updateData.doseId,
                        DoseStatus.SKIPPED,
                        updateTime
                    )
                }
            }
        }
    }

    fun onMissedClicked(){
        viewModelScope.launch {
            if(_selectedDate.value.isAfter(LocalDateTime.now())){
                val missedDate = _selectedDate.value.toLocalDate()
                val missedDateTime = missedDate.atTime(_updateDoseData.value?.doseTime ?: LocalTime.now() )
                _updateDoseData.value?.doseId?.let {
                    compositeRepository.missDose(it, missedDateTime ) }
            } else {
                _updateDoseData.value?.doseId?.let { compositeRepository.deleteDose(it) }
            }
            _showDialogState.update { UpdateDoseDialogUIState.Hidden }
        }
    }


    fun onNextDateClicked() {
        val newDate = _selectedDate.value.plusDays(1)
        _selectedDate.update { it.plusDays(1) }
    }

    fun onPreviousDateClicked() {
        val newDate = _selectedDate.value.minusDays(1)
        _selectedDate.update {newDate
        }
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


