package com.example.newtrackmed.ui.feature.addmedication

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class AddMedicationUiState(
    val addMedicationScreenState: AddMedicationScreenState,
    val addMedDialogState: AddMedDialogState,
    val currentDialog: AddMedDialog
)

@Immutable
sealed interface AddMedicationScreenState{
    object MedicationDetails: AddMedicationScreenState
    object DoseDetails: AddMedicationScreenState
    object ScheduleReminder: AddMedicationScreenState
}

@Immutable
sealed interface AddMedDialog{
    object DoseUnitDialog: AddMedDialog
    object MedTypeDialog: AddMedDialog
    object FrequencyDialog: AddMedDialog
    object FirstReminderDialog: AddMedDialog
    object MedicationDatesDialog: AddMedDialog
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
    private val _medTypeQuestionData = MedTypeQuestionData()

    private val _asNeededQuestionData = AsNeededQuestionData()

    private val _timeQuestionData = TimeQuestionData()

    private val _dosageQuestionData = DosageQuestionData()

    private val _addMedScreenData = AddMedScreenData(
        _nameQuestionData,
        _strengthQuestionData,
        _doseUnitQuestionData,
        _medTypeQuestionData,
        _asNeededQuestionData,
        _timeQuestionData,
        _dosageQuestionData
    )

    val addMedScreenData: AddMedScreenData
        get() = _addMedScreenData


    private val _addMedScreenState = MutableStateFlow<AddMedicationScreenState>(AddMedicationScreenState.MedicationDetails)
    private val _currentDialog = MutableStateFlow<AddMedDialog>(AddMedDialog.DoseUnitDialog)
    private val _showDialogState = MutableStateFlow<AddMedDialogState>(AddMedDialogState.HideDialog)

    val uiState: StateFlow<AddMedicationUiState> = combine(
        _addMedScreenState,
        _showDialogState,
        _currentDialog
    ){screenState, dialogState, curDialog ->

        val addMedScreenState: AddMedicationScreenState = when (screenState){
            is AddMedicationScreenState.MedicationDetails -> AddMedicationScreenState.MedicationDetails
            is AddMedicationScreenState.DoseDetails -> AddMedicationScreenState.DoseDetails
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


        AddMedicationUiState(
            addMedScreenState,
            showDialogState,
            selectedDialog
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AddMedicationUiState(
            AddMedicationScreenState.MedicationDetails,
            AddMedDialogState.HideDialog,
            AddMedDialog.DoseUnitDialog
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


    fun onMedNameChanged(input: String){
        _nameQuestionData.onNameChange(input)
    }

    fun onStrengthValueChanged(input: String){
        _strengthQuestionData.onStrengthChanged(input)
    }

    fun onSelectDosageUnitClicked(){
        Log.d("Debug Unit Dialog", "State: ${_showDialogState.value} Dialog: ${_currentDialog.value}")
        viewModelScope.launch {
            _currentDialog.update { AddMedDialog.DoseUnitDialog }
            _showDialogState.update { AddMedDialogState.ShowDialog }
        }
    }
    
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

    fun onAsNeededClicked(input: Boolean){
        _asNeededQuestionData.onAsNeededChanged(input)
    }

    fun onSaveMedDetailsClicked(){
//        validateMedName()
        _nameQuestionData.validateName()
//        validateStrength()
        _strengthQuestionData.validateStrength()
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

    fun validateAnswer() : Boolean{
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

class MedTypeQuestionData{
    private val _medTypeAnswer = mutableStateOf<MedTypeOption?>(null)
    val medTypeAnswer: MedTypeOption?
        get() = _medTypeAnswer.value
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
}
//TODO: Change to string resource
@Immutable
data class MedTypeOption(
    @StringRes val name: Int,
    val isSelected: Boolean = false
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

class TimeQuestionData{
    private val _timeAnswer = mutableStateOf<LocalTime>(LocalTime.of(12, 0))
    val timeAnswer = derivedStateOf { _timeAnswer.value.format(
        DateTimeFormatter.ofPattern("h:mm a")) }

    fun onTimeAnswerChanged(newTime: LocalTime){
        _timeAnswer.value = newTime
    }
}

class FrequencyQuestionData{
    private val _frequencyTypeAnswer = mutableStateOf<FrequencyType?>(null)
    val frequencyTypeAnswer: FrequencyType?
        get() = _frequencyTypeAnswer.value

    private val _frequencyIntervalAnswer = mutableStateOf<List<Int>>(emptyList())
    val frequencyIntervalAnswer: List<Int>
        get() = _frequencyIntervalAnswer.value

    private val _selectedDaysInterval = mutableStateOf<Int?>(null)
    val selectedDaysInterval: Int?
        get() = _selectedDaysInterval.value
    private val _selectedWeekDays = mutableStateOf<List<Int>>(emptyList())
    val selectedWeekDays: List<Int>
        get() = _selectedWeekDays.value
    private val _selectedMonthDays = mutableStateOf<List<Int>>(emptyList())
    val selectedMonthDays: List<Int>
        get() = _selectedMonthDays.value

    fun onFrequencyOptionSelected(type: FrequencyType){
        _frequencyTypeAnswer.value = type
    }


    fun onWeekdaySelected(weekday: Int){

    }

    fun onMonthDaySelected(day: Int){

    }



}

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
}
