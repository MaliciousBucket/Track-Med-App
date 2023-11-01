package com.example.newtrackmed.data.questiondata

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import java.text.SimpleDateFormat

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