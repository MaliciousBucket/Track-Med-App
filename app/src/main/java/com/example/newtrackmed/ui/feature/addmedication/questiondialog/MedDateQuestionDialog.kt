package com.example.newtrackmed.ui.feature.addmedication.questiondialog


import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newtrackmed.R
import com.example.newtrackmed.ui.feature.addmedication.UpdateMedDialogTitle
import com.example.newtrackmed.ui.theme.NewTrackMedTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedDatesDialogContent(
    @StringRes title: Int,
    @StringRes backButtonDescription: Int,
    initialStartDateMillis: Long? = null,
    initialEndDateMillis: Long? = null,
    onSaveClicked: ((Long?, Long?) -> Unit)? = null,
    onBackPressed: () -> Unit,
) {

    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialStartDateMillis,
        initialSelectedEndDateMillis = initialEndDateMillis
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 0.dp, 4.dp, 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { onBackPressed() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(backButtonDescription)
                    )
                }
                if (onSaveClicked != null) {
                    TextButton(
                        onClick = { onSaveClicked(state.selectedStartDateMillis, state.selectedEndDateMillis) },
                        enabled = state.selectedEndDateMillis != null && state.selectedStartDateMillis!! > state.selectedEndDateMillis!!
                    ) {
                        Text(text = stringResource(id = R.string.save))
                    }
                }
            }
            UpdateMedDialogTitle(
                title = title,
                icon = Icons.Filled.CalendarMonth,
            )
            DateRangePicker(state = state, modifier = Modifier.weight(1f))
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MedDatePickerDialogPreview() {
    NewTrackMedTheme {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            MedDatesDialogContent(title = R.string.med_dates_dialog_title, backButtonDescription = R.string.nav_back_dose_details) {
                
            }
        }
    }

}
