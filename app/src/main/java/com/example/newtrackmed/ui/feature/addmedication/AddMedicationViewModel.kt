package com.example.newtrackmed.ui.feature.addmedication

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.newtrackmed.R
import com.example.newtrackmed.data.entity.FrequencyType
import com.example.newtrackmed.data.repository.DoseRepository
import com.example.newtrackmed.data.repository.MedicationRepository
import com.example.newtrackmed.di.TrackMedApp
import com.example.newtrackmed.ui.feature.addmedication.questiondialog.DoseUnitOption
import com.example.newtrackmed.ui.feature.addmedication.questiondialog.FrequencyOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


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

    object MedicationDatesDialog: AddMedDialog
    object MedicationTimeDialog: AddMedDialog
    object FrequencyDialog: AddMedDialog
    object FirstReminderDialog: AddMedDialog
    object XDaysDialog: AddMedDialog
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

        val questionsValid = _nameQuestionData.isNameError &&
                !_strengthQuestionData.isStrengthError &&
                _doseUnitQuestionData.doseUnitAnswer.isNotEmpty() &&
                _medTypeQuestionData.medTypeAnswer.isNotEmpty()
        Log.d("Debug Save", "Value: $questionsValid")

        if (questionsValid) {
            viewModelScope.launch {
                if (_asNeededQuestionData.asNeededAnswer) {
                    _addMedScreenState.update { AddMedicationScreenState.AsNeedDoseDetails }
                } else
                _addMedScreenState.update { AddMedicationScreenState.AsNeedDoseDetails }
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

//    ----- Every Other Day -----

    fun onEveryOtherDaySelected(){
        viewModelScope.launch {
            _currentDialog.update { AddMedDialog.FirstReminderDialog }
            _showDialogState.update { AddMedDialogState.ShowDialog }
        }
    }

//    ----- Every X Days -----
    fun onEveryXDaysSelected(){
        viewModelScope.launch {
            _currentDialog.update { AddMedDialog.XDaysDialog }
            _showDialogState.update { AddMedDialogState.ShowDialog }
        }
    }
    fun onIntervalChange(interval: String){

    }

    fun onIntervalSaved(){

    }

//    ----- Week Days -----
    fun onWeekDaysSelected(){
        viewModelScope.launch {
            _currentDialog.update { AddMedDialog.XDaysDialog }
            _showDialogState.update { AddMedDialogState.ShowDialog }
        }
    }
    fun onWeekDayOptionSelected(weekday: Int){
        _frequencyQuestionData.onWeekdaySelected(weekday)
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
        _frequencyQuestionData.onMonthDaySelected(day)
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

class AddMedScreenData(
    val nameQuestionData: MedNameQuestionData,
    val strengthQuestionData: StrengthQuestionData,
    val doseUnitQuestionData: DoseUnitQuestionData,
    val medTypeQuestionData: MedTypeQuestionData,
    val asNeededQuestionData: AsNeededQuestionData,
    val timeQuestionData: TimeQuestionData,
    val dateQuestionData: DateQuestionData,
    val frequencyQuestionData: FrequencyQuestionData,
    val dosageQuestionData: DosageQuestionData

)

// ----Med Name Question -----
class MedNameQuestionData{
    private val _nameAnswer = mutableStateOf<String>("")
    val nameAnswer: String
        get() = _nameAnswer.value

    private val _isNameError = mutableStateOf<Boolean>(false)
    val isNameError: Boolean
        get() = _isNameError.value

    private val _nameErrorMessage = mutableStateOf<String>("")
    val nameErrorMessage: String
        get() = _nameErrorMessage.value

    fun onNameChange(input: String){
        _nameAnswer.value = input
    }

    fun validateName(){
        if(_nameAnswer.value.isEmpty()){
            _nameErrorMessage.value = "Please enter the Medication's name"
            _isNameError.value = true
            return
        }
        val regPattern = Regex("^[a-zA-Z0-9]*$")
        if(!_nameAnswer.value.matches(regPattern)){
            _nameErrorMessage.value = "Please only use AlphaNumeric characters"
            _isNameError.value = true
            return
        }
        _nameErrorMessage.value = ""
        _isNameError.value = false
    }


}
class StrengthQuestionData{
     private val _strengthAnswer = mutableStateOf<String>("")
    val strengthAnswer: String
        get() = _strengthAnswer.value

    private val _isStrengthError = mutableStateOf<Boolean>(false)
    val isStrengthError: Boolean
        get() = _isStrengthError.value

    private val _strengthErrorMessage = mutableStateOf<String>("")
    val strengthErrorMessage: String
        get() = _strengthErrorMessage.value

    fun onStrengthChanged(input: String){
        _strengthAnswer.value = input
    }

    fun validateStrength(){
        if(_strengthAnswer.value.isNullOrEmpty()){
            _strengthErrorMessage.value = "Please enter the strength of your Medication"
            _isStrengthError.value = true
            return
        }
        val regPattern = Regex("^[0-9]*$")
        if (!_strengthAnswer.value.isDigitsOnly()){
            _strengthErrorMessage.value = "Please use numeric characters"
            _isStrengthError.value = true
            return
        }
        _strengthErrorMessage.value = ""
        _isStrengthError.value = false
    }
}

interface ResourceWrapper {
    fun getString(@StringRes id: Int): String
}

class ContextResourceWrapper(private val context: Context) : ResourceWrapper {
    override fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }
}




class DoseUnitQuestionData(
    private val resourceWrapper: ResourceWrapper
){

    private val _doseUnitAnswer = mutableStateOf<String>("")
    val doseUnitAnswer: String
        get() = _doseUnitAnswer.value
    private val _selectedIndex = mutableIntStateOf(-1)
    val selectedIndex: Int
        get() = _selectedIndex.intValue

    private val _doseUnitOptions = listOf<DoseUnitOption>(
        DoseUnitOption(R.string.mg, R.string.milligram),
        DoseUnitOption(R.string.g, R.string.gram),
        DoseUnitOption(R.string.mcg, R.string.microgram),
        DoseUnitOption(R.string.kg, R.string.kilogram),
        DoseUnitOption(R.string.l, R.string.liter),
        DoseUnitOption(R.string.ml, R.string.milliliter),
        DoseUnitOption(R.string.iu, R.string.international_unit),
        DoseUnitOption(R.string.mmol, R.string.millimole),
        DoseUnitOption(R.string.mol, R.string.mole),
        DoseUnitOption(R.string.meq, R.string.milliequivalent),
        DoseUnitOption(R.string.u, R.string.unit),
        DoseUnitOption(R.string.cc, R.string.cubic_centimeter),
        DoseUnitOption(R.string.oz, R.string.ounce),
        DoseUnitOption(R.string.tsp, R.string.teaspoon),
        DoseUnitOption(R.string.tbsp, R.string.tablespoon)
    )
    val doseUnitOptions : List<DoseUnitOption>
        get() = _doseUnitOptions

    private val _customAnswer = mutableStateOf("")
    val customAnswer: String
        get() = _customAnswer.value

    private val _isCustomAnswerError = mutableStateOf(false)
    val isCustomAnswerError: Boolean
        get() = _isCustomAnswerError.value

    private val _errorMessage = mutableStateOf("")
    val errorMessage: String
        get() = _errorMessage.value

    private val _customAnswerSelected = mutableStateOf(false)
    val customAnswerSelected: Boolean
        get() = _customAnswerSelected.value

    fun onCustomAnswerChange(input: String){
        Log.d("Debug Custom Answer", "Recieving: $input")
        _customAnswer.value = input
    }

    fun onCustomAnswerSelected(){
        _selectedIndex.intValue = -1
        _customAnswerSelected.value = true
    }

    fun onDoseUnitOptionSelected(index: Int){
        _customAnswerSelected.value = false
        _isCustomAnswerError.value = false
        
        _selectedIndex.intValue = index
        Log.d("Debug DoeUnit", "Index value: ${_selectedIndex.intValue}")
    }

    fun onDoseUnitAnswerSaved() : Boolean{
        Log.d("Debug Selected", "Value: $_customAnswerSelected")
        val validAnswer = validateAnswer()
        if(!validAnswer){
            Log.d("Invalid Answer", "Value: ${_customAnswer.value}")
            return false
        }
        return if(_customAnswerSelected.value){
            _doseUnitAnswer.value = _customAnswer.value
            true
        } else {

            val stringAnswer = resourceWrapper.getString(_doseUnitOptions[_selectedIndex.intValue].value)
            _doseUnitAnswer.value = stringAnswer
            true
        }
    }

    private fun validateAnswer() : Boolean{
        if(customAnswerSelected){
            if(_customAnswer.value.isEmpty()){
                Log.d("Debug Validation", "Answer: ${_customAnswer.value} is empty")
                _errorMessage.value = "Please select another option or enter a custom value"
                _isCustomAnswerError.value = true
                return false
            }
            return true
        }
        if(_selectedIndex.intValue < 0){
            Log.d("Debug Validate", "Failed because of index. Value: ${_selectedIndex.intValue}")
            _errorMessage.value = "Please select an option"
            return false
        }
        Log.d("Debug validation", "Passed")
        _errorMessage.value = ""
        _isCustomAnswerError.value = false
        return true
    }



}

class MedTypeQuestionData(
    private val resourceWrapper: ResourceWrapper
){
    private val _medTypeAnswer = mutableStateOf("")
    val medTypeAnswer: String
        get() = _medTypeAnswer.value

    private val _selectedIndex = mutableIntStateOf(-1)
    val selectedIndex : Int
        get() = _selectedIndex.intValue

    private val _medTypeOptions = listOf<MedTypeOption>(
        MedTypeOption(R.string.pill),
        MedTypeOption(R.string.sachet),
        MedTypeOption(R.string.tablet),
        MedTypeOption(R.string.spray),
        MedTypeOption(R.string.topical),
        MedTypeOption(R.string.capsule),
        MedTypeOption(R.string.liquid),
        MedTypeOption(R.string.gel),
        MedTypeOption(R.string.injection),
        MedTypeOption(R.string.patch),
        MedTypeOption(R.string.inhaler),
        MedTypeOption(R.string.cream),
        MedTypeOption(R.string.ointment),
        MedTypeOption(R.string.suppository),
        MedTypeOption(R.string.lozenge)
    )
    val medTypeOptions: List<MedTypeOption>
        get() = _medTypeOptions

    private val _customAnswer = mutableStateOf("")
    val customAnswer: String
        get() = _customAnswer.value

    private val _isCustomAnswerError = mutableStateOf(false)
    val isCustomAnswerError: Boolean
        get() = _isCustomAnswerError.value

    private val _errorMessage = mutableStateOf("")
    val errorMessage: String
        get() = _errorMessage.value

    private val _customAnswerSelected = mutableStateOf(false)
    val customAnswerSelected: Boolean
        get() = _customAnswerSelected.value

    fun onCustomAnswerChange(input: String){
        _customAnswer.value = input
    }

    fun onCustomAnswerSelected(){
        _selectedIndex.intValue = -1
        _customAnswerSelected.value = true
    }

    fun onMedTypeOptionSelected(index: Int){
        _customAnswerSelected.value = false
        _isCustomAnswerError.value = false

        _selectedIndex.intValue = index
    }

    fun onMedTypeAnswerSaved() : Boolean {
        Log.d("Debug Selected", "Value: $_customAnswerSelected")
        val validAnswer = validateAnswer()
        if (!validAnswer) {
            Log.d("Invalid Answer", "Value: ${_customAnswer.value}")
            return false
        }
        return if (_customAnswerSelected.value) {
           _medTypeAnswer.value = _customAnswer.value
            true
        } else {

            val stringAnswer =
                resourceWrapper.getString(medTypeOptions[_selectedIndex.intValue].name)
            _medTypeAnswer.value = stringAnswer
            true
        }

    }

    private fun validateAnswer(): Boolean {
        val regPattern = Regex("^[a-zA-Z]*$")
        if (customAnswerSelected) {
            if (_customAnswer.value.isEmpty()) {
                Log.d("Debug Validation", "Answer: ${_customAnswer.value} is empty")
                _errorMessage.value = "Please select another option or enter a custom value"
                _isCustomAnswerError.value = true
                return false
            }
            if (!_customAnswer.value.matches(regPattern)) {
                _errorMessage.value = "Please use only alphabetical characters"
                _isCustomAnswerError.value = true
                return false
            }
            return true
        }
        if (_selectedIndex.intValue < 0) {
            Log.d("Debug Validate", "Failed because of index. Value: ${_selectedIndex.intValue}")
            _errorMessage.value = "Please select an option"
            return false
        }
        _errorMessage.value = ""
        _isCustomAnswerError.value = false
        return true

    }

}
//TODO: Change to string resource
@Immutable
data class MedTypeOption(
    @StringRes val name: Int,
)



class AsNeededQuestionData{
    private val _asNeededAnswer = mutableStateOf<Boolean>(false)
   val asNeededAnswer: Boolean
       get() = _asNeededAnswer.value
   val selectedIndex: State<Int> = derivedStateOf { if (_asNeededAnswer.value) 0 else 1 }

    val options = listOf<AsNeededOption>(
        AsNeededOption(R.string.as_needed, true ),
        AsNeededOption(R.string.scheduled, false)
    )

    fun onAsNeededChanged(newValue: Boolean){
        _asNeededAnswer.value = newValue
    }

}

data class AsNeededOption(
    val labelResourceId: Int,
    val value: Boolean,
)

class DateQuestionData{

    private val _startDateAnswer = mutableStateOf<Long?>(null)
    val startDateAnswer: Long?
        get() = _startDateAnswer.value

    private val _endDateAnswer = mutableStateOf<Long?>(null)
    val endDateAnswer: Long?
        get() = _endDateAnswer.value

    private val _formattedDateAnswer = mutableStateOf("")
    val formattedDateAnswer: String
        get() = _formattedDateAnswer.value

    fun onDatesSaved(startDate: Long?, endDate: Long?){
        _startDateAnswer.value = startDate
        _endDateAnswer.value = endDate
        formatAnswer()
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatAnswer(){
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val formattedStartDate = _startDateAnswer.value?.let { simpleDateFormat.format(it) }
        val formattedEndDate = _endDateAnswer.value?.let{simpleDateFormat.format(it)}

        _formattedDateAnswer.value = "$formattedStartDate - $formattedEndDate"

    }
}

class TimeQuestionData{
    private val _hoursAnswer = mutableStateOf<Int?>(null)
    val hoursAnswer: Int?
        get() = _hoursAnswer.value

    private val _minutesAnswer = mutableStateOf<Int?>(null)
    val minutesAnswer: Int?
        get() = _minutesAnswer.value

    private val _timeAnswer = mutableStateOf("")
    val timeAnswer: String
        get() = _timeAnswer.value

    fun onTimeAnswerSaved(hour: Int, minute: Int){
        _hoursAnswer.value = hour
        _minutesAnswer.value = minute
        formatAnswer()
    }

    private fun formatAnswer(){
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val cal = Calendar.getInstance()
        _hoursAnswer.value?.let { cal.set(Calendar.HOUR_OF_DAY, it) }
        _minutesAnswer.value?.let { cal.set(Calendar.MINUTE, it) }
        cal.isLenient = false

        _timeAnswer.value = formatter.format(cal.time)
    }
}

class FrequencyQuestionData{
    private val _frequencyTypeAnswer = mutableStateOf<FrequencyType?>(null)
    val frequencyTypeAnswer: FrequencyType?
        get() = _frequencyTypeAnswer.value

    private val _frequencyOptions = mutableStateOf<List<FrequencyOption>>(listOf(
        FrequencyOption(R.string.daily, FrequencyType.DAILY),
        FrequencyOption(R.string.every_other_day, FrequencyType.EVERY_OTHER),
        FrequencyOption(R.string.every_x_days, FrequencyType.EVERY_X_DAYS),
        FrequencyOption(R.string.days_of_the_week, FrequencyType.WEEK_DAYS),
        FrequencyOption(R.string.days_of_the_month, FrequencyType.MONTH_DAYS)
    ))

    private val _weekDayOptions: List<WeekDayOption> = listOf(
        WeekDayOption(R.string.monday, 1),
        WeekDayOption(R.string.tuesday, 2),
        WeekDayOption(R.string.wednesday, 3),
        WeekDayOption(R.string.thursday, 4),
        WeekDayOption(R.string.friday, 5),
        WeekDayOption(R.string.saturday, 6),
        WeekDayOption(R.string.sunday, 7)

    )
    val weekDayOptions: List<WeekDayOption>
        get() = _weekDayOptions

    private val _frequencyIntervalAnswer = mutableStateOf<List<Int>>(emptyList())
    val frequencyIntervalAnswer: List<Int>
        get() = _frequencyIntervalAnswer.value

    private val _selectedDaysInterval = mutableStateOf<String>("")
    val selectedDaysInterval: String
        get() = _selectedDaysInterval.value
    private val _selectedWeekDays = mutableStateListOf<Int>()
    val selectedWeekDays: List<Int>
        get() = _selectedWeekDays

    private val _selectedMonthDays = mutableStateListOf<Int>()
    val selectedMonthDays: List<Int>
        get() = _selectedMonthDays

    fun onFrequencyOptionSelected(type: FrequencyType){
        _frequencyTypeAnswer.value = type
    }

//    ----- Error Flags -----
    private val _isXDaysError = mutableStateOf(false)
    val isXDaysError: Boolean
        get() = _isXDaysError.value

    private val _isWeekDaysError = mutableStateOf(false)
    val isWeekDaysError: Boolean
        get() = _isWeekDaysError.value

    private val _isMonthDaysError = mutableStateOf(false)
    val isMonthDaysError: Boolean
        get() = _isMonthDaysError.value




//    ----- Error Messages -----


    fun onWeekdaySelected(weekday: Int){
        if(weekday in _selectedWeekDays){
            _selectedWeekDays.remove(weekday)
        } else {
            _selectedWeekDays.add(weekday)
        }
    }

    fun onMonthDaySelected(day: Int){
        if(day in _selectedMonthDays){
            _selectedMonthDays.remove(day)
        } else {
            _selectedMonthDays.add(day)
        }
    }

}
data class WeekDayOption(
    @StringRes val name: Int,
    val value: Int
)

class DosageQuestionData{
    private val _dosageAnswer = mutableStateOf<String>("")
    val dosageAnswer: String
        get() = _dosageAnswer.value

    private val _isDosageError = mutableStateOf<Boolean>(false)
    val isDosageError: Boolean
        get() = _isDosageError.value

    private val _dosageErrorMessage = mutableStateOf<String>("")
    val dosageErrorMessage: String
        get() = _dosageErrorMessage.value

    fun onDosageChanged(input: String){
        _dosageAnswer.value = input
    }
    fun validateAnswer() : Boolean{
        if(_dosageAnswer.value.isEmpty()){
            _dosageErrorMessage.value = "Please enter the interval between doses"
            _isDosageError.value = true
            return false
        }
        if(!_dosageAnswer.value.isDigitsOnly()){
            _dosageErrorMessage.value = "Please enter a number"
            _isDosageError.value = true
            return false
        }
        _dosageErrorMessage.value = ""
        _isDosageError.value = false
        return true
    }
}
