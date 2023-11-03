package com.example.newtrackmed.ui.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newtrackmed.ui.theme.NewTrackMedTheme
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@Composable
fun DateIndicator(
    selectedDate: LocalDateTime,
    onDateClicked: (LocalDateTime) -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDateTime.now().toLocalDate()
    val displayText = when (selectedDate.toLocalDate()) {
        today -> "Today, ${selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${selectedDate.dayOfMonth}"
        today.plusDays(1) -> "Tomorrow, ${selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${selectedDate.dayOfMonth}"
        today.minusDays(1) -> "Yesterday, ${selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${selectedDate.dayOfMonth}"
        else -> "${DayOfWeek.from(selectedDate).getDisplayName(TextStyle.FULL, Locale.getDefault())}, ${selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${selectedDate.dayOfMonth}"
    }

    Column(
        Modifier.fillMaxWidth()
    ) {
        WeekDisplay(
            selectedDate = selectedDate,
            onDateClicked
        )
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onPreviousClick() },
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.NavigateBefore,
                    contentDescription = "Previous Day"
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = displayText,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                softWrap = true,
                overflow = TextOverflow.Clip
            )

            IconButton(onClick = { onNextClick() },
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.NavigateNext,
                    contentDescription = "Next Day"
                )
            }
        }
    }
    HorizontalDivider()
}



@Composable
fun WeekDisplay(
    selectedDate: LocalDateTime,
    onDateClick: (LocalDateTime) -> Unit
    ) {
    val firstDayOfWeek = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val lastDayOfWeek = selectedDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

    val weekData = mutableListOf<Pair<LocalDateTime, String>>()
    var currentDate = firstDayOfWeek
    while (!currentDate.isAfter(lastDayOfWeek)) {
        weekData.add(
            Pair(
                currentDate,
                currentDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            )
        )
        currentDate = currentDate.plusDays(1)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),

        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weekData.forEach { (date, dayName) ->
            val isSelected =
                date.dayOfMonth == selectedDate.dayOfMonth && date.month == selectedDate.month
            val textStyle =
                if (isSelected) androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold) else androidx.compose.ui.text.TextStyle()

            val backgroundShape = if (isSelected) RoundedCornerShape(45) else RoundedCornerShape(0)
            val backgroundColor = if (isSelected) Color.Blue else Color.White

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .background(backgroundColor, backgroundShape)
                    .padding(2.dp)

            ) {
                Column(
                    modifier = Modifier
                        .clickable { onDateClick(date) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HomeDateContainer(text = date.dayOfMonth.toString(), isSelected = isSelected)
                    if(isSelected){
                        Row(modifier = Modifier
                            .height(2.dp)
                            .width(32.dp)
                            .background(color = MaterialTheme.colorScheme.surfaceContainer)){

                        }

                    } else {
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                    HomeDateContainer(text = dayName, isSelected = isSelected)

                }
            }
        }
    }
}

@Composable
fun HomeDateContainer(
    text: String,
    isSelected: Boolean,
) {
    val backgroundColour = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh
    val contentColour = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    OutlinedButton(
        onClick = {  },
        modifier = Modifier.size(32.dp),
        shape = CircleShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColour,
            contentColor = contentColour
        )
    ) {
        Text(
            text = text
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DateIndicatorPreview(){
    NewTrackMedTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            DateIndicator(
                selectedDate = LocalDateTime.now().minusDays(3),
                onPreviousClick = { /*TODO*/ },
                onNextClick = { /*TODO*/ },
                onDateClicked = {}
            )
        }
    }
}