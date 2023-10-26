package com.example.newtrackmed.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateIndicator(
    displayText: String,
    selectedDate: LocalDateTime,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        WeekDisplay(selectedDate = selectedDate)
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
                textAlign = TextAlign.Center
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
fun WeekDisplay(selectedDate: LocalDateTime) {
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
        modifier = Modifier.fillMaxWidth(),
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
                    .padding(8.dp)

            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = date.dayOfMonth.toString().padStart(2, '0'), style = textStyle)
                    if (isSelected) {
                        Spacer(modifier = Modifier.height(1.dp))
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = Color.White,
                            modifier = Modifier.width(28.dp)
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                    } else {
                        Spacer(modifier = Modifier.height(5.dp))
                    }

                    Text(text = dayName, style = textStyle)
                }
            }
        }
    }
}