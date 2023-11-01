package com.example.newtrackmed.ui.feature.addmedication.questiondialog

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.newtrackmed.R
import com.example.newtrackmed.ui.feature.addmedication.CustomAnswerItem
import com.example.newtrackmed.ui.feature.addmedication.DoseQuestionListItem
import com.example.newtrackmed.ui.feature.addmedication.QuestionDialogWrapper
import com.example.newtrackmed.ui.feature.updateMedication.AddMedDialogSaveButton

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DoseUnitDialogContent(
    @StringRes dialogTitleResourceId: Int,
//    questionData: DoseUnitQuestionData,
    doseUnitOptions: List<DoseUnitOption>,
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
    //TODO: Make this take the title as a aparemeter so it can be used for editing
    QuestionDialogWrapper(
        title = dialogTitleResourceId,
        icon = Icons.Default.Scale,
        backButtonDescription = R.string.nav_back_med_details,
        errorMessage = errorMessage,
        isError = isCustomAnswerError,
        onSaveClicked = onSaveClicked,
        onBackPressed = onBackPressed
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
                Log.d("Debug Custom Answer", "New value: $newValue")
                onCustomAnswerChange(newValue)},
            onOptionSelected = { onCustomAnswerSelected() }
        )

        LazyColumn(
            modifier = modifier
        ) {
            itemsIndexed(doseUnitOptions) { index, item ->
                DoseQuestionListItem(
                    value = stringResource(item.value),
                    extendedName = stringResource(item.extendedValue),
                    isSelected = selectedIndex == index,
                    onOptionSelected = {
                        Log.d("Debug DoseUnit", "Composable selected: $index")
                        onItemSelected(index) })
            }
        }
        AddMedDialogSaveButton(text = R.string.custom) {
            
        }
    }

}



data class DoseUnitOption(
    @StringRes val value: Int,
    @StringRes val extendedValue: Int,
)

@Preview(showBackground = true)
@Composable
fun MedDetailsScreenPreview() {
//    val testData = DoseUnitQuestionData()
//    NewTrackMedTheme {
//        Column(
//            modifier = Modifier.padding(16.dp),
//            verticalArrangement = Arrangement.Center
//        ){
//            DoseUnitDialogContent(
//                doseUnitOptions = testData.doseUnitOptions,
//                selectedIndex = -1,
//                customAnswer = "",
//                errorMessage = "Please select another option or enter a custom value",
//                customAnswerSelected = false,
//                isCustomAnswerError = true,
//                onCustomAnswerChange = {},
//                onCustomAnswerSelected = { /*TODO*/ },
//                onItemSelected = {},
//                onBackPressed = { /*TODO*/ })
//        }
//    }
//}
}