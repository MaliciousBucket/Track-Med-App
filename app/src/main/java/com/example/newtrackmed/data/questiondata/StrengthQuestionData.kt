package com.example.newtrackmed.data.questiondata

import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly

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