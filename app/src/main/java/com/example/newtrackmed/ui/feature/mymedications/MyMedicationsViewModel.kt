package com.example.newtrackmed.ui.feature.mymedications

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import com.example.newtrackmed.data.Result
import com.example.newtrackmed.data.asResult
import com.example.newtrackmed.data.model.MyMedicationsViewData
import com.example.newtrackmed.data.repository.MedicationDoseCompositeRepository
import com.example.newtrackmed.di.TrackMedApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.newtrackmed.data.entity.FrequencyEntity
import com.example.newtrackmed.data.entity.MedicationEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class MyMedicationsUiState(
    val myMedicationsOverview: MyMedicationsOverviewUiState,
    val myMedicationsScreen: MyMedicationsScreenUiState,
    val displayMedication: DisplayMedication,
    val displayFrequency: DisplayMedFrequency
)


@Immutable
sealed interface MyMedicationsOverviewUiState {
    data class Success(val myMedicationsViewData: List<MyMedicationsViewData>) :
        MyMedicationsOverviewUiState
    object Loading : MyMedicationsOverviewUiState
    object Error : MyMedicationsOverviewUiState
}

@Immutable
sealed interface MyMedicationsScreenUiState {
    object Overview: MyMedicationsScreenUiState
    object Details: MyMedicationsScreenUiState
}

@Immutable
sealed interface DisplayMedication {
    data class Success( val medication: MedicationEntity): DisplayMedication
    object Loading: DisplayMedication
    object Error: DisplayMedication
}

@Immutable
sealed interface DisplayMedFrequency {
    data class Success(val frequency: FrequencyEntity) : DisplayMedFrequency
    object Loading: DisplayMedFrequency
    object Error: DisplayMedFrequency
}



class MyMedicationsViewModel(
 private val compositeRepository: MedicationDoseCompositeRepository
): ViewModel() {

    //TODO: Change to 0 before finish
    private val _medicationIdForDisplay = MutableStateFlow<Int>(1)

    private val _myMedications: Flow<Result<List<MyMedicationsViewData>>> =
        compositeRepository.getMyMedicationsViewData().asResult()
//    val myMedications = _myMedications

    private val _screenUiState = MutableStateFlow<MyMedicationsScreenUiState>(MyMedicationsScreenUiState.Overview)

    private val _displayMedication: Flow<Result<MedicationEntity>> =
        _medicationIdForDisplay.map { it }.distinctUntilChanged().flatMapLatest { id ->
            compositeRepository.getMedicationForDisplay(id).asResult()
        }
    private val _displayFrequency: Flow<Result<FrequencyEntity>> =
        _medicationIdForDisplay.map { it }.distinctUntilChanged().flatMapLatest { id ->
            compositeRepository.getFrequencyForDisplay(id).asResult()
        }


    val uiState: StateFlow<MyMedicationsUiState> = combine(
        _myMedications,
        _screenUiState,
        _displayMedication,
        _displayFrequency
    ){ myMeds, screenUiState, displayMed, displayFreq ->

        val myMedsState: MyMedicationsOverviewUiState = when(myMeds){
            is Result.Success -> MyMedicationsOverviewUiState.Success(myMeds.data)
            is Result.Loading -> MyMedicationsOverviewUiState.Loading
            is Result.Error -> MyMedicationsOverviewUiState.Error
        }

        val screenState: MyMedicationsScreenUiState = when (screenUiState) {
            is MyMedicationsScreenUiState.Overview -> MyMedicationsScreenUiState.Overview
            is MyMedicationsScreenUiState.Details -> MyMedicationsScreenUiState.Details
        }

        val medicationForDisplay: DisplayMedication = when (displayMed){
            is Result.Success -> DisplayMedication.Success(displayMed.data)
            is Result.Loading -> DisplayMedication.Loading
            is Result.Error -> DisplayMedication.Error
        }

        val frequencyForDisplay: DisplayMedFrequency = when (displayFreq) {
            is Result.Success -> DisplayMedFrequency.Success(displayFreq.data)
            is Result.Loading -> DisplayMedFrequency.Loading
            is Result.Error -> DisplayMedFrequency.Error
        }

        MyMedicationsUiState(
            myMedsState,
            screenState,
            medicationForDisplay,
            frequencyForDisplay
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MyMedicationsUiState(
            MyMedicationsOverviewUiState.Loading,
            MyMedicationsScreenUiState.Overview,
            DisplayMedication.Loading,
            DisplayMedFrequency.Loading
        )
    )

    fun onMyMedsListItemClicked(medicationId: Int){
        Log.d("Debug List Item Clicked", "Item Clicked: $medicationId")
        viewModelScope.launch {
            _medicationIdForDisplay.update { medicationId }
            _screenUiState.update { MyMedicationsScreenUiState.Details }

        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appModule = TrackMedApp.appModule
                val compositeRepository = appModule.compositeRepository

                MyMedicationsViewModel(
                    compositeRepository = compositeRepository
                )

            }
        }
    }


}