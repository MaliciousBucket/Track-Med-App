package com.example.newtrackmed.ui.feature.addmedication

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.newtrackmed.data.entity.FrequencyType
import com.example.newtrackmed.data.questiondata.AddMedScreenData
import com.example.newtrackmed.data.questiondata.AsNeededQuestionData
import com.example.newtrackmed.data.questiondata.DateQuestionData
import com.example.newtrackmed.data.questiondata.DosageQuestionData
import com.example.newtrackmed.data.questiondata.DoseUnitQuestionData
import com.example.newtrackmed.data.questiondata.FrequencyQuestionData
import com.example.newtrackmed.data.questiondata.MedNameQuestionData
import com.example.newtrackmed.data.questiondata.MedTypeQuestionData
import com.example.newtrackmed.data.questiondata.StrengthQuestionData
import com.example.newtrackmed.data.questiondata.TimeQuestionData
import com.example.newtrackmed.data.repository.DoseRepository
import com.example.newtrackmed.data.repository.MedicationRepository
import com.example.newtrackmed.di.TrackMedApp
import com.example.newtrackmed.util.ResourceWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddMedicationUiState(
    val addMedicationScreenState: AddMedicationScreenState,
    val addMedDialogState: AddMedDialogState,
    val currentDialog: AddMedDialog,
    val saveMedDetailsBtnState: Boolean
)

@Immutable
sealed interface AddMedicationScreenState{
    object MedicationDetails: AddMedicationScreenState
    object ScheduledDoseDetails: AddMedicationScreenState
    object AsNeedDoseDetails: AddMedicationScreenState
    object ScheduleReminder: AddMedicationScreenState
}

@Immutable
sealed interface AddMedDialog{
    object DoseUnitDialog: AddMedDialog
    object MedTypeDialog: AddMedDialog
    object MedicationTimeDialog: AddMedDialog
    object MedicationDatesDialog: AddMedDialog
    object FrequencyDialog: AddMedDialog
    object FirstReminderDialog: AddMedDialog
    object IntervalDaysDialog: AddMedDialog
    object WeekDaysDialog: AddMedDialog
    object MonthDaysDialog: AddMedDialog
    object SetReminderDialog: AddMedDialog

}

@Immutable
sealed interface AddMedDialogState{
    object ShowDialog: AddMedDialogState
    object HideDialog: AddMedDialogState
}



class AddMedicationViewModel(
    private val doseRepository: DoseRepository,
    private val medicationRepository: MedicationRepository,
    private val resourceWrapper: ResourceWrapper,
): ViewModel() {




    private val _nameQuestionData = MedNameQuestionData()

    private val _strengthQuestionData = StrengthQuestionData()
    private val _doseUnitQuestionData = DoseUnitQuestionData(resourceWrapper)
    private val _medTypeQuestionData = MedTypeQuestionData(resourceWrapper)

    private val _asNeededQuestionData = AsNeededQuestionData()

    private val _timeQuestionData = TimeQuestionData()
    private val _dateQuestionData = DateQuestionData()
    private val _frequencyQuestionData = FrequencyQuestionData()
    private val _dosageQuestionData = DosageQuestionData()

    private val _addMedScreenData = AddMedScreenData(
        _nameQuestionData,
        _strengthQuestionData,
        _doseUnitQuestionData,
        _medTypeQuestionData,
        _asNeededQuestionData,
        _timeQuestionData,
        _dateQuestionData,
        _frequencyQuestionData,
        _dosageQuestionData
    )

    val addMedScreenData: AddMedScreenData
        get() = _addMedScreenData


    private val _addMedScreenState = MutableStateFlow<AddMedicationScreenState>(AddMedicationScreenState.MedicationDetails)
    private val _currentDialog = MutableStateFlow<AddMedDialog>(AddMedDialog.DoseUnitDialog)
    private val _showDialogState = MutableStateFlow<AddMedDialogState>(AddMedDialogState.HideDialog)
    private val _savedMedDetailsButtonEnabled = MutableStateFlow<Boolean>(true)

    val uiState: StateFlow<AddMedicationUiState> = combine(
        _addMedScreenState,
        _showDialogState,
        _currentDialog,
        _savedMedDetailsButtonEnabled
    ){screenState, dialogState, curDialog, saveMedBtnState ->

        val addMedScreenState: AddMedicationScreenState = when (screenState){
            is AddMedicationScreenState.MedicationDetails -> AddMedicationScreenState.MedicationDetails
            is AddMedicationScreenState.ScheduledDoseDetails -> AddMedicationScreenState.ScheduledDoseDetails
            is AddMedicationScreenState.AsNeedDoseDetails -> AddMedicationScreenState.AsNeedDoseDetails
            is AddMedicationScreenState.ScheduleReminder -> AddMedicationScreenState.ScheduleReminder

            else -> {
                AddMedicationScreenState.MedicationDetails
            }
        }

        val showDialogState: AddMedDialogState = when (dialogState){
            is AddMedDialogState.ShowDialog -> AddMedDialogState.ShowDialog
            is AddMedDialogState.HideDialog -> AddMedDialogState.HideDialog
            else ->{
                AddMedDialogState.HideDialog
            }
        }

        val selectedDialog = _currentDialog.value

        val medDetailsButtonState = _savedMedDetailsButtonEnabled.value

        AddMedicationUiState(
            addMedScreenState,
            showDialogState,
            selectedDialog,
            medDetailsButtonState
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AddMedicationUiState(
            AddMedicationScreenState.MedicationDetails,
            AddMedDialogState.HideDialog,
            AddMedDialog.DoseUnitDialog,
            true
        )
    )


    fun onDialogDismissRequest(){
        viewModelScope.launch {
            _showDialogState.update {AddMedDialogState.HideDialog }
        }
    }


    
    fun getSaveDialogEnabled(): Boolean{
        when (_currentDialog.value) {
            is AddMedDialog.DoseUnitDialog -> return !_doseUnitQuestionData.isCustomAnswerError
            else -> {
                return true
            }
        }
    }

//    ----- Med Details Screen ------

    fun onSelectDosageUnitClicked(){
        Log.d("Debug Unit Dialog", "State: ${_showDialogState.value} Dialog: ${_currentDialog.value}")
        viewModelScope.launch {
            _currentDialog.update { AddMedDialog.DoseUnitDialog }
            _showDialogState.update { AddMedDialogState.ShowDialog }
        }
    }

    fun onSelectMedTypeClicked(){
        viewModelScope.launch {
            _currentDialog.update { AddMedDialog.MedTypeDialog }
            _showDialogState.update { AddMedDialogState.ShowDialog }
        }
    }



    //  ----- Medication Name -----
    fun onMedNameChanged(input: String){
        _nameQuestionData.onNameChange(input)
    }

//    ----- Medication Strength -----

    fun onStrengthValueChanged(input: String){
        _strengthQuestionData.onStrengthChanged(input)
    }

//    ----- Dose Unit -----
    
    fun onCustomDoseUnitChanged(input: String){
        Log.d("Debug CustomAnswer", "Passing In: $input")
        _doseUnitQuestionData.onCustomAnswerChange(input)
    }

    fun onDoseUnitOptionSelected(index: Int){
        Log.d("Debug DoseUnit", "Passing In: $index")
        _doseUnitQuestionData.onDoseUnitOptionSelected(index)
    }
    
    fun onCustomDoseUnitSelected(){
        _doseUnitQuestionData.onCustomAnswerSelected()
    }
    
    fun onSaveDoseUnitClicked(){
        if (_doseUnitQuestionData.onDoseUnitAnswerSaved()){
            _showDialogState.value = AddMedDialogState.HideDialog
            Log.d("Success", "Value: ${_doseUnitQuestionData.doseUnitAnswer}")
        } else {
            Log.d("Failure", "Value of custom: ${_doseUnitQuestionData.customAnswer}")
        }
    }

//    ----- Medication Type -----
    fun onCustomMedTypeAnswerChanged(input: String){
        _medTypeQuestionData.onCustomAnswerChange(input)
    }

    fun onCustomMedTypeSelected(){
        _medTypeQuestionData.onCustomAnswerSelected()
    }

    fun onMedTypeOptionSelected(index: Int){
        _medTypeQuestionData.onMedTypeOptionSelected(index)
    }

    fun onSaveMedTypeClicked(){
        if(_medTypeQuestionData.onMedTypeAnswerSaved()){
            _showDialogState.value = AddMedDialogState.HideDialog
        }
    }

//     ----- As Needed -----



    fun onAsNeededClicked(input: Boolean){
        _asNeededQuestionData.onAsNeededChanged(input)
    }

//    ----- Save Med Details -----

    fun onSaveMedDetailsClicked(){
        _nameQuestionData.validateName()
        _strengthQuestionData.validateStrength()

        val questionsValid = !_nameQuestionData.isNameError &&
                !_strengthQuestionData.isStrengthError &&
                _doseUnitQuestionData.doseUnitAnswer.isNotEmpty() &&
                _medTypeQuestionData.medTypeAnswer.isNotEmpty()
        Log.d("Debug Save", "Value: $questionsValid")

        if (questionsValid) {
            viewModelScope.launch {
                if (_asNeededQuestionData.asNeededAnswer) {
                    _addMedScreenState.update { AddMedicationScreenState.AsNeedDoseDetails }
                } else {
                    _addMedScreenState.update { AddMedicationScreenState.ScheduledDoseDetails }
                }
            }

        } else {
            viewModelScope.launch {
                _savedMedDetailsButtonEnabled.update { false }
            }
            return
        }


    }

//    ----- Dose Details Screens -----

    fun onsSelectTimeClicked(){
        viewModelScope.launch {
            _currentDialog.update { AddMedDialog.MedicationTimeDialog }
            _showDialogState.update { AddMedDialogState.ShowDialog }
        }
    }
    fun onSelectDatesClick(){
        viewModelScope.launch {
            _currentDialog.update { AddMedDialog.MedicationDatesDialog }
            _showDialogState.update { AddMedDialogState.ShowDialog }
        }
    }



//  ----- Date Range Question -----



    fun onDatesSaved(startDate: Long?, endDate: Long?){
        _dateQuestionData.onDatesSaved(startDate, endDate)
    }

//  ------ Time Question ----

    fun onTimeSaved(hour: Int, minute: Int){
        _timeQuestionData.onTimeAnswerSaved(hour, minute)
    }

//    ----- Frequency -----
    fun onSelectFrequencyClick(){
        viewModelScope.launch {
            _currentDialog.update { AddMedDialog.FrequencyDialog }
            _showDialogState.update { AddMedDialogState.ShowDialog }
        }
    }

    fun onDailySelected(){
        viewModelScope.launch {
            _frequencyQuestionData.onFrequencyTypeSelected(FrequencyType.DAILY)
            _showDialogState.update { AddMedDialogState.HideDialog }
        }
    }

//    ----- Every Other Day -----

    fun onEveryOtherDaySelected(){
        viewModelScope.launch {
            _currentDialog.update { AddMedDialog.FirstReminderDialog }
            _showDialogState.update { AddMedDialogState.ShowDialog }
        }
    }

//    ----- Interval Days -----
    fun onEveryXDaysSelected(){
        viewModelScope.launch {
            _currentDialog.update { AddMedDialog.IntervalDaysDialog }
            _showDialogState.update { AddMedDialogState.ShowDialog }
        }
    }
    fun onIntervalChange(interval: String){
        _frequencyQuestionData.onIntervalDaysChange(interval)
    }

    fun onIntervalSaved(){

    }

//    ----- Week Days -----
    fun onWeekDaysSelected(){
        viewModelScope.launch {
            _currentDialog.update { AddMedDialog.WeekDaysDialog }
            _showDialogState.update { AddMedDialogState.ShowDialog }
        }
    }
    fun onWeekDayOptionSelected(weekday: Int){
        _frequencyQuestionData.onWeekdayOptionSelected(weekday)
    }

    fun onWeekDaysSaved(){

    }

//    ----- Month Days -----
    fun onMonthDaysSelected(){
    viewModelScope.launch {
        _currentDialog.update { AddMedDialog.MonthDaysDialog }
        _showDialogState.update { AddMedDialogState.ShowDialog }
    }
    }
    fun onMonthDayOptionsSelected(day: Int){
        _frequencyQuestionData.onMonthDayOptionSelected(day)
    }

    fun onMonthDaysSaved(){

    }

//    ----- Dosage -----

    fun onDosageChanged(input: String){
        _dosageQuestionData.onDosageChanged(input)
    }

    fun onDosageSaved(){
        if(!_dosageQuestionData.validateAnswer()){
            return
        }
        else {
            viewModelScope.launch {
                _showDialogState.update { AddMedDialogState.HideDialog }
            }
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appModule = TrackMedApp.appModule
                val doseRepository = appModule.doseRepository
                val medicationRepository = appModule.medicationRepository
                val resourceWrapper = appModule.resourceWrapper

                AddMedicationViewModel(
                    doseRepository = doseRepository,
                    medicationRepository = medicationRepository,
                    resourceWrapper = resourceWrapper
                )
            }
        }
    }
}





