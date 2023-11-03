package com.example.newtrackmed.data.questiondata

import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import com.example.newtrackmed.R
import com.example.newtrackmed.data.entity.FrequencyType
import com.example.newtrackmed.ui.feature.addmedication.questiondialog.FrequencyOption

class FrequencyQuestionData{
    private val _frequencyTypeAnswer = mutableStateOf<FrequencyType?>(null)
    val frequencyTypeAnswer: FrequencyType?
        get() = _frequencyTypeAnswer.value

    private val _formattedAnswer = mutableStateOf("")
    val formattedAnswer:String
        get() = _formattedAnswer.value

    private val _frequencyOptions = mutableStateOf<List<FrequencyOption>>(
        listOf(
            FrequencyOption(R.string.daily, FrequencyType.DAILY),
            FrequencyOption(R.string.every_other_day, FrequencyType.EVERY_OTHER),
            FrequencyOption(R.string.every_x_days, FrequencyType.EVERY_X_DAYS),
            FrequencyOption(R.string.days_of_the_week, FrequencyType.WEEK_DAYS),
            FrequencyOption(R.string.days_of_the_month, FrequencyType.MONTH_DAYS)
        )
    )

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

    private val _intervalDaysAnswer = mutableStateOf<String>("")
    val intervalDaysAnswer: String
        get() = _intervalDaysAnswer.value

    private val _selectedWeekDays = mutableStateListOf<Int>()
    val selectedWeekDays: List<Int>
        get() = _selectedWeekDays

    private val _selectedMonthDays = mutableStateListOf<Int>()
    val selectedMonthDays: List<Int>
        get() = _selectedMonthDays

//    ----- Error Flags -----
    private val _isIntervalDaysError = mutableStateOf(false)
    val isIntervalDaysError: Boolean
        get() = _isIntervalDaysError.value

    private val _isWeekDaysError = mutableStateOf(false)
    val isWeekDaysError: Boolean
        get() = _isWeekDaysError.value

    private val _isMonthDaysError = mutableStateOf(false)
    val isMonthDaysError: Boolean
        get() = _isMonthDaysError.value


//    ----- Error Messages -----
    private val _intervalDaysErrorMessage = mutableStateOf("")
    val intervalDaysErrorMessage: String
        get() = _intervalDaysErrorMessage.value

    private val _weekDaysErrorMessage = mutableStateOf("")
    val weekDaysErrorMessage: String
        get() = _weekDaysErrorMessage.value

    private val _monthDaysErrorMessage = mutableStateOf("")
    val monthDaysErrorMessage: String
        get() = _monthDaysErrorMessage.value

//    ----- Interval Days -----

    fun onFrequencyTypeSelected(frequencyType: FrequencyType){
        _frequencyTypeAnswer.value = frequencyType
    }

    fun onIntervalDaysChange(input: String){
        _intervalDaysAnswer.value = input
    }

    fun validateIntervalDays(): Boolean{
        if(_intervalDaysAnswer.value.isEmpty()){
            _intervalDaysErrorMessage.value = "Please enter the interval between doses"
            _isIntervalDaysError.value = true
            return false
        }
        if(!_intervalDaysAnswer.value.isDigitsOnly()){
            _intervalDaysErrorMessage.value = "Please enter a valid number"
            _isIntervalDaysError.value = true
            return false
        }
        _intervalDaysErrorMessage.value = ""
        _isIntervalDaysError.value = false
        return true
    }

    fun onIntervalDaysSaved() : Boolean{
        return if (!validateIntervalDays()){
            false
        } else {
            _frequencyTypeAnswer.value = FrequencyType.EVERY_X_DAYS
            _formattedAnswer.value = "Take every ${_frequencyIntervalAnswer.value} days"
            true
        }
    }

//    ----- Week Days -----

    fun onWeekdayOptionSelected(weekday: Int){
        if(weekday in _selectedWeekDays){
            _selectedWeekDays.remove(weekday)
        } else {
            _selectedWeekDays.add(weekday)
        }
    }

    fun validateWeekDays(): Boolean{
        return if(_selectedWeekDays.isEmpty()){
            _weekDaysErrorMessage.value = "Please select at least 1 days"
            _isWeekDaysError.value = true
            false
        } else {
            _weekDaysErrorMessage.value = ""
            _isWeekDaysError.value = false
            true
        }
    }

    fun onWeekDaysSaved(): Boolean{
        return if(!validateWeekDays()){
            false
        } else {

            _formattedAnswer.value = "TODO"
            true
        }
    }
//  ------ Month Days -----
    fun onMonthDayOptionSelected(day: Int){
        if(day in _selectedMonthDays){
            _selectedMonthDays.remove(day)
        } else {
            _selectedMonthDays.add(day)
        }
    }

    fun validateMonthDays(): Boolean{
        return if(_selectedMonthDays.isEmpty()){
            _monthDaysErrorMessage.value = "Please select at least 1 day"
            _isMonthDaysError.value = true
            false
        } else {
            _monthDaysErrorMessage.value = ""
            _isMonthDaysError.value = false
            true
        }
    }

    fun onMonthDaysSaved() : Boolean{
        return if (!validateMonthDays()){
            false
        } else {
            _formattedAnswer.value = "Take on the TODODODO"
            true
        }
    }

    fun validateSelectedFrequencies(): Boolean {

        return when (_frequencyTypeAnswer.value) {
            FrequencyType.DAILY -> {
                true
            }
            FrequencyType.EVERY_OTHER -> {
                true
            }

            FrequencyType.EVERY_X_DAYS -> {
                return _intervalDaysAnswer.value.isNotEmpty()
                        && _isIntervalDaysError.value
            }

            FrequencyType.WEEK_DAYS -> {
                return _selectedWeekDays.isNotEmpty()
                        && _isWeekDaysError.value
            }

            FrequencyType.MONTH_DAYS -> {
                return _selectedMonthDays.isNotEmpty()
                        && _isMonthDaysError.value
            }
            null -> {
                false
            }
        }
    }
}

data class WeekDayOption(
    @StringRes val name: Int,
    val value: Int
)