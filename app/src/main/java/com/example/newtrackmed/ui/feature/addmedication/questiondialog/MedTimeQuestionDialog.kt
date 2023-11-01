package com.example.newtrackmed.ui.feature.addmedication.questiondialog

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newtrackmed.R
import com.example.newtrackmed.ui.feature.addmedication.QuestionDialogWrapper
import com.example.newtrackmed.ui.theme.NewTrackMedTheme
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedTimeDialogContent(
    @StringRes dialogTitleResourceId: Int,
    @StringRes backButtonDescription: Int,
    initialTimeHour: Int?,
    initialTimeMinute: Int?,
    onSaveClicked: (Int, Int) -> Unit,
    onBackPressed: () -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = initialTimeHour ?: LocalTime.now().hour,
        initialMinute = initialTimeMinute ?: LocalTime.now().minute
    )

    QuestionDialogWrapper(
        title = dialogTitleResourceId,
        icon = Icons.Sharp.Schedule,
        backButtonDescription = backButtonDescription,
        errorMessage = "",
        isError = false,
        onSaveClicked = { onSaveClicked(state.hour, state.minute) },
        onBackPressed = onBackPressed
    ) {
        TimePicker(state)
    }
}




@Preview(showBackground = true)
@Composable
fun TrackMedTimePickerPreview() {
    NewTrackMedTheme {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            MedTimeDialogContent(
                dialogTitleResourceId = R.string.med_time_dialog_title,
                backButtonDescription = R.string.nav_back_dose_details,
                initialTimeHour = 10,
                initialTimeMinute = 35,
                onSaveClicked ={ hour, minute ->

                }
            ) {

            }
        }
    }
}