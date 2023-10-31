package com.example.newtrackmed.ui.feature.addmedication

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.newtrackmed.R
import com.example.newtrackmed.ui.feature.updateMedication.AddMedicationTextQuestion
import com.example.newtrackmed.ui.feature.updateMedication.MedicationNavQuestion

@Composable
fun AddScheduledDetailsScreen(
    modifier: Modifier = Modifier,
    timeAnswer: String,
    dateAnswer: String,
    frequencyAnswer: String,
    dosageAnswer: String,
    dosageErrorMessage: String,
    isDosageError: Boolean,
    onSelectTimeClicked: () -> Unit,
    onSelectDateClicked: () -> Unit,
    onSelectFrequencyClicked: () -> Unit,
    onDosageValueChange: (String) -> Unit
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        MedTimeQuestion(
            titleResourceId = R.string.edit_med_time,
            answer = timeAnswer) {
            onSelectTimeClicked()
        }
        MedDateQuestion(
            titleResourceId = R.string.enter_med_dates,
            answer = dateAnswer
        ) {
            onSelectDateClicked()
        }
        MedFrequencyQuestion(
            titleResourceId = R.string.enter_med_frequency,
            answer = frequencyAnswer) {
            onSelectFrequencyClicked()
        }
        MedDosageQuestion(
            titleResourceId = R.string.enter_med_dosage,
            input = dosageAnswer,
            errorMessage = dosageErrorMessage,
            isError = isDosageError,
            onValueChange = {newValue ->
                onDosageValueChange(newValue)}
        )
    }
}

@Composable
fun AsNeededDetailsScreen(
    dateAnswer: String,
    dosageAnswer: String,
    dosageErrorMessage: String,
    isDosageError: Boolean,
    onDosageValueChange: (String) -> Unit,
    onSelectDateClicked: () -> Unit,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        MedDateQuestion(
            titleResourceId = R.string.enter_med_dates,
            answer = dateAnswer
        ) {
            onSelectDateClicked()
        }
        MedDosageQuestion(
            titleResourceId = R.string.enter_med_dosage,
            input = dosageAnswer,
            errorMessage = dosageErrorMessage,
            isError = isDosageError,
            onValueChange = { newValue ->
                onDosageValueChange(newValue)
            }
        )
    }
}

//val formattedTimeAnswer: State<String> = derivedStateOf {
//    _timeAnswer.value.format(DateTimeFormatter.ofPattern("h:mm a"))
//}
@Composable
fun MedTimeQuestion(
    @StringRes titleResourceId: Int,
    answer: String,

    onSelectTimeClicked: () -> Unit
){
    MedicationNavQuestion(
        titleResourceId = titleResourceId,
        placeholderResourceId = R.string.enter_med_time_placeholder,
        iconDescriptionResourceId = R.string.nav_to_enter_time,
        answer = answer,
        iconImage = Icons.Default.Timer
    ) {
        onSelectTimeClicked()
    }
}

@Composable
fun MedDateQuestion(
    @StringRes titleResourceId: Int,
    answer: String,
    onSelectDateClicked: () -> Unit
){
    MedicationNavQuestion(
        titleResourceId = titleResourceId,
        placeholderResourceId = R.string.enter_med_dates_placeholder,
        iconDescriptionResourceId = R.string.nav_to_enter_dates,
        answer = answer,
        iconImage = Icons.Filled.CalendarMonth
    ) {
        onSelectDateClicked()
    }
}


@Composable
fun MedFrequencyQuestion(
    @StringRes titleResourceId: Int,
    answer: String,

    onSelectFrequencyClicked: () -> Unit
){
    MedicationNavQuestion(
        titleResourceId = titleResourceId,
        placeholderResourceId = R.string.enter_med_frequency_placeholder,
        iconDescriptionResourceId = R.string.nav_to_select_frequency,
        answer = answer,
        iconImage = Icons.Filled.CalendarMonth
    ) {
        onSelectFrequencyClicked()
    }
}

@Composable
fun MedDosageQuestion(
    @StringRes titleResourceId: Int,
    input: String,
    errorMessage: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
){
    AddMedicationTextQuestion(
        titleResourceId = titleResourceId,
        placeholderResourceId = R.string.enter_med_dosage_placeholder,
        input = input,
        iconImage = Icons.Filled.Medication,
        keyboardType = KeyboardType.NumberPassword,
        isInputError = isError,
        errorMessage = errorMessage,
        onValueChange = onValueChange
    )
}


