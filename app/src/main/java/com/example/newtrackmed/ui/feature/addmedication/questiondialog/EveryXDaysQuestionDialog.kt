package com.example.newtrackmed.ui.feature.addmedication.questiondialog

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.newtrackmed.R
import com.example.newtrackmed.ui.feature.addmedication.QuestionDialogWrapper

@Composable
fun EveryXDaysDialogContent(
    @StringRes title: Int,
    @StringRes backButtonDescription: Int,
    answer: String,
    isError: Boolean,
    errorMessage: String,
    onValueChange: (String) -> Unit,
    onBackPressed: () -> Unit
){
    val keyboardController = LocalSoftwareKeyboardController.current
    QuestionDialogWrapper(
        title = title,
        icon = Icons.Filled.Numbers,
        backButtonDescription = R.string.nav_back_frequency_options,
        onSaveClicked = { /*TODO*/ },
        onBackPressed = { /*TODO*/ }) {
        Box(
            modifier = Modifier.padding(24.dp)
        ) {
            BasicTextField(
                value = answer,
                onValueChange = {newValue ->
                    onValueChange(newValue)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.NumberPassword),
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()})
            )
        }
    }
}