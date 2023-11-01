package com.example.newtrackmed.ui.feature.addmedication.questiondialog

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newtrackmed.ui.feature.addmedication.QuestionDialogWrapper

@Composable
fun MonthDaysQuestionDialogContent(
    @StringRes title: Int,
    @StringRes backButtonDescription: Int,
    selectedDates: List<Int>,
    isError: Boolean,
    errorMessage: String,
    onItemClicked: (Int) -> Unit,
    onSaveClicked: () -> Unit,
    onBackPressed: () -> Unit
){
    QuestionDialogWrapper(
        title = title,
        icon = Icons.Filled.DateRange,
        backButtonDescription = backButtonDescription,
        errorMessage = errorMessage,
        isError = isError,
        onSaveClicked = { onSaveClicked()},
        onBackPressed = { onBackPressed() }) {
        DisplayMonthDays(
            selectedDates = selectedDates,
            onDateClicked = {onItemClicked(it)}
        )
    }
}

//TODO: Use this with the date picker
@Composable
fun MonthDayDisplay(
    date: Int,
    isSelected: Boolean,
    onDateClick: (Int) -> Unit
) {
    val backgroundColour = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh
    val contentColour = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    OutlinedButton(
        onClick = { onDateClick(date) },
        modifier = Modifier.size(28.dp),
        shape = CircleShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColour,
            contentColor = contentColour
        )
    ) {
        Text(
            text = date.toString()
        )
    }
}

@Composable
fun DisplayMonthDays(
    selectedDates: List<Int>,
    onDateClicked: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier
            .padding(8.dp)
            .width(208.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        items(31) { index ->
            val dateNumber = index + 1
            MonthDayDisplay(
                date = dateNumber,
                isSelected = selectedDates.contains(dateNumber)
            ) { onDateClicked(it) }
        }
    }

}