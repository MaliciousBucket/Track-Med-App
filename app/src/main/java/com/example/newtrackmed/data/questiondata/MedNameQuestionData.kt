package com.example.newtrackmed.data.questiondata

import androidx.compose.runtime.mutableStateOf

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