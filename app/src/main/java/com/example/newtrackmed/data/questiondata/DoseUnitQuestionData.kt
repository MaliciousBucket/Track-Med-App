package com.example.newtrackmed.data.questiondata

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.example.newtrackmed.R
import com.example.newtrackmed.util.ResourceWrapper
import com.example.newtrackmed.ui.feature.addmedication.questiondialog.DoseUnitOption

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