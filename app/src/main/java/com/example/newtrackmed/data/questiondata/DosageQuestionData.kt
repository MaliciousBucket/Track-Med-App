package com.example.newtrackmed.data.questiondata

import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly

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