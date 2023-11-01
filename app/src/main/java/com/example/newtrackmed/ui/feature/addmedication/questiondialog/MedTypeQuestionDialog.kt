package com.example.newtrackmed.ui.feature.addmedication.questiondialog

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.newtrackmed.R
import com.example.newtrackmed.ui.feature.addmedication.CustomAnswerItem
import com.example.newtrackmed.ui.feature.addmedication.MedQuestionListOption
import com.example.newtrackmed.data.questiondata.MedTypeOption
import com.example.newtrackmed.ui.feature.addmedication.QuestionDialogWrapper
import com.example.newtrackmed.ui.feature.updateMedication.AddMedDialogSaveButton

@Composable
fun MedTypeDialogContent(
    @StringRes dialogTitleResourceId: Int,
    typeOptions: List<MedTypeOption>,
    selectedIndex: Int,
    customAnswer: String,
    errorMessage: String,
    customAnswerSelected: Boolean,
    isCustomAnswerError: Boolean,
    onCustomAnswerChange: (String) -> Unit,
    onCustomAnswerSelected: () -> Unit,
    onItemSelected: (Int) -> Unit,
    onSaveClicked: () -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
){
    QuestionDialogWrapper(
        title = dialogTitleResourceId,
        icon = Icons.Filled.Medication,
        backButtonDescription = R.string.nav_back_med_details,
        errorMessage = errorMessage,
        isError = isCustomAnswerError,
        onSaveClicked = { onSaveClicked()},
        onBackPressed = { onBackPressed() }
    ) {
        if(errorMessage.isNotEmpty()){
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = errorMessage,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }

        CustomAnswerItem(
            value = customAnswer,
            isError = isCustomAnswerError,
            isSelected = customAnswerSelected,
            onValueChange = {newValue ->
                onCustomAnswerChange(newValue)},
            onOptionSelected = { onCustomAnswerSelected() }
        )

        LazyColumn(
            modifier = modifier
        ) {
            itemsIndexed(typeOptions) { index, item ->
                MedQuestionListOption(
                    text = stringResource(item.name),
                    isSelected = selectedIndex == index,
                    onOptionSelected = { onItemSelected(index) })
            }
        }
        AddMedDialogSaveButton(text = R.string.custom) {
        }
    }
}