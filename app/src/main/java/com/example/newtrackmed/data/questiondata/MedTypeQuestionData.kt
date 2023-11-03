package com.example.newtrackmed.data.questiondata

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.example.newtrackmed.R
import com.example.newtrackmed.util.ResourceWrapper

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