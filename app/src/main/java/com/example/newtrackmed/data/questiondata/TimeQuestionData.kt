package com.example.newtrackmed.data.questiondata

import androidx.compose.runtime.mutableStateOf
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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