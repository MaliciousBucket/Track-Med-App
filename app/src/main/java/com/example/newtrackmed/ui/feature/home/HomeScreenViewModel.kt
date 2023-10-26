package com.example.newtrackmed.ui.feature.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.example.newtrackmed.data.model.DoseViewData
import com.example.newtrackmed.data.model.UpdateDoseData
import com.example.newtrackmed.data.repository.MedicationDoseCompositeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime

data class HomeUiState(
    val displayedDoses: DoseDisplayUIState,
    val updateDoseDialog: UpdateDoseDialogUIState,
    val selectedDate: SelectedDateUIState,
    val isError: Boolean,
)

@Immutable
sealed interface DoseDisplayUIState {
    data class Success(val DoseViewData: List<DoseViewData>) : DoseDisplayUIState
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
        val displayText: String,
        val selectedDate: LocalDateTime
    ): SelectedDateUIState
    data class Loading(val displayText: String,)

}



class HomeScreenViewModel(
    private val compositeRepository: MedicationDoseCompositeRepository
) : ViewModel(){

    private val selectedDate = MutableStateFlow(LocalDateTime.now())


}