package com.example.newtrackmed.ui.feature.addmedication

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newtrackmed.R
import com.example.newtrackmed.data.questiondata.AsNeededOption
import com.example.newtrackmed.data.questiondata.AsNeededQuestionData
import com.example.newtrackmed.data.questiondata.DoseUnitQuestionData
import com.example.newtrackmed.data.questiondata.MedNameQuestionData
import com.example.newtrackmed.data.questiondata.MedTypeQuestionData
import com.example.newtrackmed.data.questiondata.StrengthQuestionData
import com.example.newtrackmed.ui.feature.updateMedication.AddMedSaveButton
import com.example.newtrackmed.ui.feature.updateMedication.AddMedicationTextQuestion
import com.example.newtrackmed.ui.feature.updateMedication.MedicationNavQuestion
import com.example.newtrackmed.ui.feature.updateMedication.MedicationQuestionCard
import com.example.newtrackmed.ui.theme.NewTrackMedTheme


@Composable
fun AddMedDetailsScreen(
    nameQuestionData: MedNameQuestionData,
    strengthQuestionData: StrengthQuestionData,
    doseUnitQuestionData: DoseUnitQuestionData,
    medTypeQuestionData: MedTypeQuestionData,
    asNeededQuestionData: AsNeededQuestionData,
    onNameValueChange: (String) -> Unit,
    onStrengthValueChange: (String) -> Unit,
    onSelectDoseUnitClicked: () -> Unit,
    onSelectTypeClicked: () -> Unit,
    onAsNeededOptionSelected: (Boolean) -> Unit,
    onSaveMedDetailsClicked: () -> Unit,
    isSaveButtonError: Boolean,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MedNameQuestion(
            titleResourceId = R.string.enter_med_name,
            input = nameQuestionData.nameAnswer,
            isError = nameQuestionData.isNameError,
            errorMessage = nameQuestionData.nameErrorMessage,
            onValueChange = onNameValueChange
        )
        MedStrengthQuestion(
            titleResourceId = R.string.enter_med_strength,
            input = strengthQuestionData.strengthAnswer,
            isError = strengthQuestionData.isStrengthError,
            errorMessage = strengthQuestionData.strengthErrorMessage,
            onValueChange = onStrengthValueChange
        )

        DosageUnitQuestion(
            titleResourceId = R.string.enter_dose_unit,
            answer = doseUnitQuestionData.doseUnitAnswer
        ) {
            onSelectDoseUnitClicked()
        }

        MedTypeQuestion(
            titleResourceId = R.string.enter_med_type,
            answer= medTypeQuestionData.medTypeAnswer,
//            questionData = medTypeQuestionData) {
        ){
            onSelectTypeClicked()
        }
        AsNeededQuestion(
            titleResourceId = R.string.enter_med_as_needed_or_scheduled,
            selectedIndex = asNeededQuestionData.selectedIndex.value,
            options = asNeededQuestionData.options,
            onOptionSelected = onAsNeededOptionSelected
        )
        AddMedSaveButton(text = R.string.save_medication_details, isEnabled = true) {
            onSaveMedDetailsClicked()
        }
    }
}
//R.string.enter_med_name
@Composable
fun MedNameQuestion(
    @StringRes titleResourceId: Int,
    input: String,
    isError: Boolean,
    errorMessage: String,
    onValueChange: (String) -> Unit
){
    AddMedicationTextQuestion(
        titleResourceId = R.string.enter_med_name,
        placeholderResourceId = R.string.enter_med_name_placeholder,
        input = input,
        iconImage = Icons.Filled.Edit,
        keyboardType = KeyboardType.Text,
        isInputError = isError,
        errorMessage = errorMessage,
        onValueChange = onValueChange
    )
}

//@Composable
//fun MedNameQuestion(
//    @StringRes titleResourceId: Int,
//    questionData: MedNameQuestionData,
//    onValueChange: (String) -> Unit
//){
//    AddMedicationTextQuestion(
//        titleResourceId = titleResourceId,
//        placeholderResourceId = R.string.enter_med_name_placeholder,
//        input = questionData.nameAnswer.value,
//        iconImage = Icons.Filled.Edit,
//        keyboardType = KeyboardType.Text,
//        isInputError = questionData.isNameError.value,
//        errorMessage = questionData.nameErrorMessage.value,
//        onValueChange = onValueChange
//    )
//
//    AddMedicationTextQuestion(
//        titleResourceId = titleResourceId,
//        placeholderResourceId = R.string.enter_med_name_placeholder,
//        input = questionData.nameAnswer,
//        iconImage = Icons.Filled.Edit,
//        keyboardType = KeyboardType.Text,
//        isInputError = questionData.isNameError,
//        errorMessage = questionData.nameErrorMessage,
//        onValueChange = onValueChange
//    )
//}
//R.string.enter_med_strength
@Composable
fun MedStrengthQuestion(
    @StringRes titleResourceId: Int,
    input: String,
    isError: Boolean,
    errorMessage: String,
    onValueChange: (String) -> Unit
){
    AddMedicationTextQuestion(
        titleResourceId = titleResourceId,
        placeholderResourceId = R.string.enter_med_strength_placeholder,
        input = input,
        iconImage = Icons.Filled.FitnessCenter,
        keyboardType = KeyboardType.NumberPassword,
        isInputError = isError,
        errorMessage = errorMessage,
        onValueChange = onValueChange
    )
}
//@Composable
//fun MedStrengthQuestion(
//    @StringRes titleResourceId: Int,
//    questionData: StrengthQuestionData,
//    onValueChange: (String) -> Unit
//){
//    AddMedicationTextQuestion(
//        titleResourceId = titleResourceId,
//        placeholderResourceId = R.string.enter_med_strength_placeholder,
//        input = questionData.strengthAnswer.value,
//        iconImage = Icons.Filled.FitnessCenter,
//        keyboardType = KeyboardType.NumberPassword,
//        isInputError = questionData.isStrengthError.value,
//        errorMessage = questionData.strengthErrorMessage.value,
//        onValueChange = onValueChange
//    )
//}
//R.string.enter_med_type

@Composable
fun DosageUnitQuestion(
    @StringRes titleResourceId: Int,
    answer: String,
    onSelectDoseUnitClicked: () -> Unit,
){
    MedicationNavQuestion(
        titleResourceId = titleResourceId,
        placeholderResourceId = R.string.enter_dose_unit_placeholder,
        iconDescriptionResourceId = R.string.nav_select_dose_unit,
        answer = answer,
        iconImage = Icons.Default.Scale
    ) {
        onSelectDoseUnitClicked()
    }
}

@Composable
fun MedTypeQuestion(
    @StringRes titleResourceId: Int,
    answer: String,
//    questionData: MedTypeQuestionData,
    onSelectTypeClicked: () -> Unit
){
    MedicationNavQuestion(
        titleResourceId = titleResourceId,
        placeholderResourceId = R.string.enter_med_type_placeholder,
        iconDescriptionResourceId = R.string.navigate_to_select_med_type,
        answer = answer,
        iconImage = Icons.Filled.Medication
    ) {
        onSelectTypeClicked()
    }
}

//private val _selectedOption = MutableState<Boolean>(false)
//val selectedOption: State<Boolean> = _selectedOption
//
//val selectedOptionAsInt: Int
//    get() = if (_selectedOption.value) 1 else 0
//R.string.enter_med_as_needed_or_scheduled
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsNeededQuestion(
    @StringRes titleResourceId: Int,
    selectedIndex: Int,
    options: List<AsNeededOption>,
    onOptionSelected: (Boolean) -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        MedicationQuestionCard(
            titleResourceId = titleResourceId,
            iconImage = Icons.Filled.Schedule
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(0.dp),
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SingleChoiceSegmentedButtonRow(){
                    options.forEachIndexed { index, option ->
                        SegmentedButton(
                            selected = index == selectedIndex,
                            onClick = { onOptionSelected(option.value)},
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            )
                        ) {
                            Text(text = stringResource(option.labelResourceId))
                        }
                    }

                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MedDetailsScreenPreview() {
    NewTrackMedTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
            ) {
            val testData = MedNameQuestionData()

        }

    }
}
