package com.example.newtrackmed.ui.feature.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.newtrackmed.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    totalDailyDosesCount: Int,
    dailyDoseProgress: Int,
    onAddDoseMedClicked: () -> Unit,
    onAddMedClicked: () -> Unit,
    onAddDoseClicked: () -> Unit,
    onCalendarClicked: () -> Unit,
    menuExpanded: Boolean,
    onDropDownMenuDismissRequest: () -> Unit

){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(text = stringResource(R.string.app_name))
            },
            actions = {
                CalendarButton {
                    onCalendarClicked()
                }
                AddDoseMedButton {
                    onAddDoseMedClicked()
                }

                AddMedDoseMenu(
                    onAddMedClicked = { onAddMedClicked() },
                    onAddDoseClicked = { onAddDoseClicked() },
                    menuExpanded = menuExpanded
                ) {

                }

            }
        )

        val animatedProgress by animateFloatAsState(
            targetValue = dailyDoseProgress / totalDailyDosesCount.toFloat(),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
            label = dailyDoseProgress.toString()
        )

        LinearProgressIndicator(
            progress = {
                animatedProgress
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            trackColor = MaterialTheme.colorScheme.onSurface
        )
    }

}

@Composable
fun CalendarButton(
    onCalendarClicked: () -> Unit
){
    IconButton(
        onClick = { onCalendarClicked() }) {
        Icon(
            imageVector = Icons.Filled.CalendarMonth,
            contentDescription = stringResource(R.string.select_date)
        )
    }
}

@Composable
fun AddDoseMedButton(
    onAddDoseMedClicked: () -> Unit
){
    IconButton(onClick = { onAddDoseMedClicked() }) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_dose_med)
        )
    }
}

@Composable
fun AddMedDoseMenu(
    onAddMedClicked: () -> Unit,
    onAddDoseClicked: () -> Unit,
    menuExpanded: Boolean,
    onDropDownMenuDismissRequest: () -> Unit
){
    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { onDropDownMenuDismissRequest() }
    ) {
        DropdownMenuItem(
            text = { Text(text = stringResource(R.string.new_medication)) },
            onClick = { onAddMedClicked()},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = stringResource(R.string.add_medication)
                )
            }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(R.string.new_dose)) },
            onClick = { onAddDoseClicked() },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Medication,
                    contentDescription = stringResource(R.string.add_dose)
                )
            }
        )

    }
}