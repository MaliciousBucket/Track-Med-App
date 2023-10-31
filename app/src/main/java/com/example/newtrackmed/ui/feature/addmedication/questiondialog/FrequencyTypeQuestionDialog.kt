package com.example.newtrackmed.ui.feature.addmedication.questiondialog

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.newtrackmed.R
import com.example.newtrackmed.data.entity.FrequencyType
import com.example.newtrackmed.ui.feature.addmedication.FrequencyQuestionNavOption
import com.example.newtrackmed.ui.feature.addmedication.MedQuestionListOption
import com.example.newtrackmed.ui.feature.addmedication.MedQuestionNavOption
import com.example.newtrackmed.ui.feature.addmedication.QuestionDialogWrapper

@Composable
fun FrequencyTypeDialogContent(
    selectedFrequency: FrequencyType?,
    onDailyClicked: () -> Unit,
    onEveryOtherDayClicked: () -> Unit,
    onEveryXDaysClicked: () -> Unit,
    onWeekDaysClicked: () -> Unit,
    onMonthDaysClicked: () -> Unit,

    onBackPressed: () -> Unit
){

    val daily = FrequencyOption(R.string.daily, FrequencyType.DAILY)
    val everyOther = FrequencyOption(R.string.every_other_day, FrequencyType.EVERY_OTHER)
    val everyXDays = FrequencyOption(R.string.every_x_days, FrequencyType.EVERY_X_DAYS)
    val weekDays = FrequencyOption(R.string.days_of_the_week, FrequencyType.WEEK_DAYS)
    val monthDays = FrequencyOption(R.string.days_of_the_month, FrequencyType.MONTH_DAYS)
    QuestionDialogWrapper(
        title = R.string.frequency_dialog_title,
        icon = Icons.Default.CalendarToday,
        backButtonDescription = R.string.nav_back_dose_details,
        onSaveClicked = null,
        onBackPressed = { onBackPressed()}) {

        MedQuestionListOption(
            text = stringResource(id = daily.name),
            isSelected = selectedFrequency == daily.frequency,
            onOptionSelected = { onDailyClicked() }
        )
        MedQuestionListOption(
            text = stringResource(id = everyOther.name),
            isSelected = selectedFrequency == everyOther.frequency,
            onOptionSelected = { onEveryOtherDayClicked() }
        )

        MedQuestionNavOption(
            title = everyXDays.name,
            isSelected = selectedFrequency == everyXDays.frequency,
            onOptionClicked = { onEveryXDaysClicked() }
        )
        MedQuestionNavOption(
            title = weekDays.name,
            isSelected = selectedFrequency == weekDays.frequency,
            onOptionClicked = { onWeekDaysClicked() }
        )
        MedQuestionNavOption(
            title = monthDays.name,
            isSelected = selectedFrequency == monthDays.frequency,
            onOptionClicked = { onMonthDaysClicked() }
        )

    }
}

data class FrequencyOption(
    @StringRes val name: Int,
    val frequency: FrequencyType
)