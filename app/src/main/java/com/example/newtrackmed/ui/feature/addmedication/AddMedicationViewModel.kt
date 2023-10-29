package com.example.newtrackmed.ui.feature.addmedication

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.newtrackmed.R
import com.example.newtrackmed.data.repository.DoseRepository
import com.example.newtrackmed.data.repository.MedicationRepository
import com.example.newtrackmed.di.TrackMedApp
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Immutable
sealed interface AddMedicationScreenState{
    object MedicationDetails: AddMedicationScreenState
    object DoseDetails: AddMedicationScreenState
    object ScheduleReminder: AddMedicationScreenState
}

//@Immutable
//sealed class AddMedicationDialog{
//    object FormDialog: AddMedicationDialog()
//    object FrequencyDialog: AddMedicationDialog()
//    object FirstReminderDialog: AddMedicationDialog()
//    object MedicationDatesDialog: AddMedicationDialog()
//    object SetReminderDialog: AddMedicationDialog()
//
//}



class AddMedicationViewModel(
    private val doseRepository: DoseRepository,
    private val medicationRepository: MedicationRepository
): ViewModel() {

    private val _nameQuestionData = MedNameQuestionData()

    private val _strengthQuestionData = StrengthQuestionData()
    private val _medTypeQuestionData = MedTypeQuestionData()

    private val _asNeededQuestionData = AsNeededQuestionData()

    private val _timeQuestionData = TimeQuestionData()

    private val _dosageQuestionData = DosageQuestionData()

    private val _addMedScreenData = AddMedScreenData(
        _nameQuestionData,
        _strengthQuestionData,
        _medTypeQuestionData,
        _asNeededQuestionData,
        _timeQuestionData,
        _dosageQuestionData
    )

    val addMedScreenData: AddMedScreenData
        get() = _addMedScreenData


    fun onMedNameChanged(input: String){
        _nameQuestionData.nameAnswer.value = input
    }

    fun onStrengthValueChanged(input: String){
        _strengthQuestionData.strengthAnswer.value = input
    }

    fun onAsNeededClicked(input: Boolean){
        _asNeededQuestionData.asNeededAnswer.value = input
    }

    fun onSaveMedDetailsClicked(){
        validateMedName()
        validateStrength()
    }

    private fun validateMedName(){
        val name = _nameQuestionData.nameAnswer.value

        if(name.isEmpty()){
            _nameQuestionData.nameErrorMessage.value = "Please enter the Medication's name"
            _nameQuestionData.isNameError.value = true
            return
        }
        val regPattern = Regex("^[a-zA-Z0-9]*$")
        if(!name.matches(regPattern)){
            _nameQuestionData.nameErrorMessage.value = "Please only use AlphaNumeric characters"
            _nameQuestionData.isNameError.value = true
            return
        }
        _nameQuestionData.nameErrorMessage.value = ""
        _nameQuestionData.isNameError.value = false
    }

    private fun validateStrength(){
        val strength = _strengthQuestionData.strengthAnswer.value

        if(_strengthQuestionData.strengthAnswer.value.isNullOrEmpty()){
            _strengthQuestionData.strengthErrorMessage.value = "Please enter the strength of your Medication"
            _strengthQuestionData.isStrengthError.value = true
            return
        }
        val regPattern = Regex("^[0-9]*$")
        if (!_strengthQuestionData.strengthAnswer.value.isDigitsOnly()){
            _strengthQuestionData.strengthErrorMessage.value = "Please use numeric characters"
            _strengthQuestionData.isStrengthError.value = true
            return
        }
        _strengthQuestionData.strengthErrorMessage.value = ""
        _strengthQuestionData.isStrengthError.value = false
    }





    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appModule = TrackMedApp.appModule
                val doseRepository = appModule.doseRepository
                val medicationRepository = appModule.medicationRepository

                AddMedicationViewModel(
                    doseRepository = doseRepository,
                    medicationRepository = medicationRepository
                )
            }
        }
    }
}

class AddMedScreenData(
    val nameQuestionData: MedNameQuestionData,
    val strengthQuestionData: StrengthQuestionData,
    val medTypeQuestionData: MedTypeQuestionData,
    val asNeededQuestionData: AsNeededQuestionData,
    val timeQuestionData: TimeQuestionData,

    val dosageQuestionData: DosageQuestionData

)
//TODO: Remove all the bullshit private public crap
class MedNameQuestionData{
    val nameAnswer = mutableStateOf<String>("")
//    val nameAnswer: String
//        get() = _nameAnswer.value
    val isNameError = mutableStateOf<Boolean>(false)
//    val isNameError: Boolean
//        get() = _isNameError.value

    val nameErrorMessage = mutableStateOf<String>("")
//    val nameErrorMessage: String
//        get() = _nameErrorMessage.value
}
class StrengthQuestionData{
     val strengthAnswer = mutableStateOf<String>("")
//    val strengthAnswer: String
//        get() = _strengthAnswer.value

    val isStrengthError = mutableStateOf<Boolean>(false)
//    val isStrengthError: Boolean
//        get() = _isStrengthError.value

    val strengthErrorMessage = mutableStateOf<String>("")
//    val strengthErrorMessage: String
//        get() = _strengthErrorMessage.value
}

class MedTypeQuestionData{
    private val _medTypeAnswer = mutableStateOf<MedTypeOption?>(null)
    val medTypeAnswer: MedTypeOption?
        get() = _medTypeAnswer.value
    val options = listOf<MedTypeOption>(
        MedTypeOption("Pill"),
        MedTypeOption("Sachet"),
        MedTypeOption("Tablet"),
        MedTypeOption("Spray"),
        MedTypeOption("Topical")
    )
}
//TODO: Change to string resource
@Immutable
data class MedTypeOption(
    val text: String,
    val isSelected: Boolean = false
)



class AsNeededQuestionData{
   val asNeededAnswer = mutableStateOf<Boolean>(false)
   val selectedIndex: State<Int> = derivedStateOf { if (asNeededAnswer.value) 0 else 1 }
    val options = listOf<AsNeededOption>(
        AsNeededOption(R.string.as_needed, true ),
        AsNeededOption(R.string.scheduled, false)
    )
}

data class AsNeededOption(
    val labelResourceId: Int,
    val value: Boolean,
)

class TimeQuestionData{
    private val _timeAnswer = mutableStateOf<LocalTime>(LocalTime.of(12, 0))
    val timeAnswer = derivedStateOf { _timeAnswer.value.format(
        DateTimeFormatter.ofPattern("h:mm a")) }
}

class FrequencyQuestionData{

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
