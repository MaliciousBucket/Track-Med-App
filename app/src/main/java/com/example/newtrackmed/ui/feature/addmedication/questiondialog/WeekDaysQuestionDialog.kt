package com.example.newtrackmed.ui.feature.addmedication.questiondialog

import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewWeek
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.newtrackmed.ui.feature.addmedication.DialogErrorMessage
import com.example.newtrackmed.ui.feature.addmedication.MedQuestionListOption
import com.example.newtrackmed.ui.feature.addmedication.QuestionDialogWrapper
import com.example.newtrackmed.ui.feature.addmedication.WeekDayOption
@Composable
fun WeekDaysQuestionDialogContent(
    @StringRes title: Int,
    @StringRes backButtonDescription: Int,
    options: List<WeekDayOption>,
    selectedIndex: List<Int>,
    isError: Boolean,
    errorMessage: String,
    onItemClicked: (Int) -> Unit,
    onSaveClicked: () -> Unit,
    onBackPressed: () -> Unit
){
    QuestionDialogWrapper(
        title = title,
        icon = Icons.Filled.ViewWeek,
        backButtonDescription = backButtonDescription,
        errorMessage = errorMessage,
        isError = isError,
        onSaveClicked = onSaveClicked,
        onBackPressed = onBackPressed) {
        DialogErrorMessage(errorMessage = errorMessage, isError = isError)
        LazyColumn {
            itemsIndexed(
                options,
                key = {_, option: WeekDayOption ->
                    option.hashCode()
                }
            ){ _, option ->
                MedQuestionListOption(
                    text = stringResource(id = option.name),
                    isSelected = option.value in selectedIndex,
                    onOptionSelected = { onItemClicked(option.value) }
                )
            }
        }
    }
}