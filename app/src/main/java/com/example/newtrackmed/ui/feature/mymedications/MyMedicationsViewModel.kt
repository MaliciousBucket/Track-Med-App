package com.example.newtrackmed.ui.feature.mymedications

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
import com.example.newtrackmed.data.entity.MedicationEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map


data class MyMedicationsUiState(
    val myMedicationsOverview: MyMedicationsOverviewUiState,
    val myMedicationsScreen: MyMedicationsScreenUiState,
    val displayMedication: DisplayMedication
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


    val uiState: StateFlow<MyMedicationsUiState> = combine(
        _myMedications,
        _screenUiState,
        _displayMedication
    ){ myMeds, screenUiState, displayMed ->

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

        MyMedicationsUiState(
            myMedsState,
            screenState,
            medicationForDisplay
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MyMedicationsUiState(
            MyMedicationsOverviewUiState.Loading,
            MyMedicationsScreenUiState.Overview,
            DisplayMedication.Loading
        )
    )

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