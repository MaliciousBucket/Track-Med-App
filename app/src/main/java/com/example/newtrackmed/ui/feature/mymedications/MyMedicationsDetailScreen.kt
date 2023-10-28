package com.example.newtrackmed.ui.feature.mymedications

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newtrackmed.R
import com.example.newtrackmed.data.entity.FrequencyType
import com.example.newtrackmed.data.entity.MedicationEntity
import com.example.newtrackmed.data.previewMedicationEntities
import com.example.newtrackmed.ui.component.ErrorScreenDisplay
import com.example.newtrackmed.ui.component.LoadingScreenDisplay
import com.example.newtrackmed.ui.theme.NewTrackMedTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MyMedsDetailScreen(
    medUiState: DisplayMedication,
    frequencyUiState: DisplayMedFrequency,
    modifier: Modifier = Modifier,
    onEditNameStrengthClicked: () -> Unit,
    onEditNotesInstructionsClicked: () -> Unit,
    onEditTimeClicked: () -> Unit,
    onEditFrequencyClicked: () -> Unit,
    onAsNeededClicked: () -> Unit,
    onIsActiveClicked: () -> Unit
){
    when (medUiState) {

        is DisplayMedication.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MedicationDetailsNameStrengthCard(
                    medication = medUiState.medication,
                    onMeNameStrengthClicked = {onEditNameStrengthClicked()}
                )
                NotesInstructionsDetailsCard(
                    notes = medUiState.medication.notes,
                    instructions = medUiState.medication.instructions,
                    onEditNotesInstructionsClicked = { onEditNotesInstructionsClicked() }
                )
                MedicationDetailsFrequencyCard(
                    displayMedUiState = medUiState,
                    frequencyUiState = frequencyUiState,
                    onMedTimeDetailsClicked = { onEditTimeClicked() },
                    onFrequencyDetailsClick = { onEditFrequencyClicked() },
                    onAsNeededChanged = {onAsNeededClicked()}
                )
                MedDetailsIsActiveCard(
                    isActive = medUiState.medication.isActive,
                    onAsNeededChanged = {onAsNeededClicked()}
                )

            }
        }

        is DisplayMedication.Loading -> {
            LoadingScreenDisplay(title = R.string.loading_my_meds_details_screen_message)
        }

        is DisplayMedication.Error -> {
            ErrorScreenDisplay(title = R.string.loading_med_details_error)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationDetailsNameStrengthCard(
    medication: MedicationEntity,
    onMeNameStrengthClicked: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.medication_details),
            style = MaterialTheme.typography.titleMedium,
            textDecoration = TextDecoration.Underline
        )
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(0.dp),
            onClick = {onMeNameStrengthClicked()}
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = medication.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "${medication.dosage}${medication.dosageUnit} ${medication.type}(s)",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                    contentDescription = stringResource(R.string.edit_name_strength)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesInstructionsDetailsCard(
    notes: String?,
    instructions: String?,
    onEditNotesInstructionsClicked: () -> Unit
){
    OutlinedCard(
        shape = RoundedCornerShape(0.dp),
        onClick = {onEditNotesInstructionsClicked()}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.notes_and_instructions),
                    style = MaterialTheme.typography.titleMedium
                )

                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    imageVector = Icons.Filled.Edit,
                    contentDescription = stringResource(R.string.edit_notes_instructions)
                )
            }
            HorizontalDivider()
            ExpandableDetailsCard(
                title = R.string.notes,
                content = notes
            )
            HorizontalDivider()
            ExpandableDetailsCard(
                title = R.string.instructions,
                content = instructions
            )
        }
    }
}

@Composable
fun ExpandableDetailsCard(
    @StringRes title: Int,
    content: String?
){
    var expanded by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                    contentDescription = stringResource(R.string.expand_or_collapse),
                    modifier = Modifier
                        .padding(4.dp)
                        .graphicsLayer(rotationZ = if (expanded) 90f else 0f)
                )
            }
            if (expanded) {
                if(content != null)
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(4.dp)
                    )
            }
        }
        HorizontalDivider()
    }
}

@Composable
fun MedicationDetailsFrequencyCard(
    displayMedUiState: DisplayMedication,
    frequencyUiState: DisplayMedFrequency,
    onMedTimeDetailsClicked: () -> Unit,
    onFrequencyDetailsClick: () -> Unit,
    onAsNeededChanged: () -> Unit
){
    Column(
        
    ) {
        Text(
            text = "Frequency",
            style = MaterialTheme.typography.titleMedium,
            textDecoration = TextDecoration.Underline
        )
        Column(
            
        ) {
            when (displayMedUiState){
                is DisplayMedication.Success -> {
                    MedFrequencyDetailsCard(
                        frequencyUiState = frequencyUiState,
                        onFrequencyDetailsClick = { onFrequencyDetailsClick()}
                    )
                    MedTimeDetailsCard(
                        medication = displayMedUiState.medication,
                        onMedTimeDetailsClicked = {onMedTimeDetailsClicked()}
                    )
                    MedStartEndDateDetailsCard(
                        startDate = displayMedUiState.medication.startDate,
                        endDate = displayMedUiState.medication.endDate
                    )
                    MedDetailsAsNeededSwitch(
                        frequencyUiState = frequencyUiState,
                        onAsNeededChanged = {onAsNeededChanged()}
                        )
                }

                is DisplayMedication.Loading -> {

                }

                is DisplayMedication.Error -> {

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedFrequencyDetailsCard(
    frequencyUiState: DisplayMedFrequency,
    onFrequencyDetailsClick: () -> Unit
){
    when (frequencyUiState) {
        is DisplayMedFrequency.Success -> {
            val frequency = frequencyUiState.frequency
            val (primaryText, secondaryText) = getFrequencyTypeText(
                frequency.frequencyType,
                frequency.asNeeded,
                frequency.frequencyIntervals
            )
            OutlinedCard(
                shape = RoundedCornerShape(0.dp),
                onClick = {
                    onFrequencyDetailsClick()
                }
            ) {
                Row (
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = primaryText,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = secondaryText,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Icon(
                        modifier = Modifier
                            .size(32.dp),
                        imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                        contentDescription = stringResource(R.string.edit_frequency)
                    )
                }
            }

        }
        is DisplayMedFrequency.Loading -> {
            MyMedsDetailsLoadingCard(title = R.string.loading_frequency_details)
        }

        is DisplayMedFrequency.Error -> {
            MyMedsDetailsErrorCard(title = R.string.loading_frequency_error)
        }
    }
}

//TODO: Refactor to use string resources
//TODO: Refactor to account for int instead of string
fun getFrequencyTypeText(type: FrequencyType, asNeeded: Boolean, interval: List<Int>) : Pair<String, String>{
    if(asNeeded){
        return Pair("As Needed", "Take as needed")
    }
    return when (type){
        FrequencyType.DAILY -> {
            Pair("Daily", "Take every day")
        }
        FrequencyType.EVERY_OTHER -> {
            Pair("Every Other Day", "Take every second day")
        }
        FrequencyType.EVERY_X_DAYS -> {
            Pair("Every $interval Days", "Take every $interval days")
        }
        FrequencyType.WEEK_DAYS -> {
            Pair("Weekly", "Take on the following days: $interval")
        }
        FrequencyType.MONTH_DAYS -> {
            Pair("Monthly", "Take on the following days of each month: $interval")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedTimeDetailsCard(
     medication: MedicationEntity,
    onMedTimeDetailsClicked: () -> Unit
){
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    val formattedTime = medication.timeToTake.format(formatter)
    OutlinedCard(
        shape = RoundedCornerShape(0.dp),
        onClick = { onMedTimeDetailsClicked()}
    ) {
        Row (
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Take ${medication.unitsTaken} ${medication.dosage} ${medication.type}(s)",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Icon(
                modifier = Modifier
                    .size(32.dp),
                imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                contentDescription = stringResource(R.string.edit_med_time)
            )
        }

    }
}

fun formatStartEndDate(startDate: LocalDate, endDate: LocalDate): Pair<String, String> {
    val formattedStartDate = formatDate(startDate)
    val formattedEndDate = formatDate(endDate)
    return Pair(formattedStartDate, formattedEndDate)
}

private fun formatDate(localDate: LocalDate): String {
    val day = localDate.dayOfMonth
    val month = localDate.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
    val dayOfWeek = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)

    val daySuffix = when (day) {
        1, 21, 31 -> "st"
        2, 22 -> "nd"
        3, 23 -> "rd"
        else -> "th"
    }

    return "$dayOfWeek, ${day}${daySuffix} of $month"
}

@Composable
fun MedStartEndDateDetailsCard(
    startDate: LocalDate,
    endDate: LocalDate
){
    val (formattedStartDate, formattedEndDate) = formatStartEndDate(startDate, endDate)

    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val formattedStartDateSimple = startDate.format(dateFormatter)
    val formattedEndDateSimple = endDate.format(dateFormatter)
    OutlinedCard(
        modifier = Modifier
            .padding(0.dp, 16.dp, 0.dp, 16.dp),
        shape = RoundedCornerShape(0.dp),
    ) {
        Row (
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = "Start Date: $formattedStartDateSimple",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "You started taking this medication on: $formattedStartDate",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = "End Date: $formattedEndDateSimple",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "You are due to stop taking this medication on: $formattedEndDate",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                contentDescription = stringResource(R.string.edit_med_time)
            )
        }
    }

}

@Composable
fun MedDetailsAsNeededSwitch(
    frequencyUiState: DisplayMedFrequency,
    onAsNeededChanged: () -> Unit
){
    when (frequencyUiState) {
        is DisplayMedFrequency.Success -> {
            OutlinedCard (
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp),
            ){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Take As Needed",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = frequencyUiState.frequency.asNeeded,
                        onCheckedChange = {
                            onAsNeededChanged()
                        },
                        thumbContent = if (frequencyUiState.frequency.asNeeded) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            }
                        } else {
                            null
                        }
                    )
                }
            }
        }
        is DisplayMedFrequency.Loading -> {
            MyMedsDetailsLoadingCard(title = R.string.loading_frequency_details)
        }

        is DisplayMedFrequency.Error -> {
            MyMedsDetailsErrorCard(title = R.string.loading_as_needed_error)
        }
    }

}

@Composable
fun MedDetailsIsActiveCard(
    isActive: Boolean,
    onAsNeededChanged: () -> Unit
){
    val title = if (isActive) R.string.med_is_active else R.string.med_is_not_active

    OutlinedCard (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleMedium
            )
            Switch(
                checked = isActive,
                onCheckedChange = {
                    onAsNeededChanged()
                },
                thumbContent = if (isActive) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyMedicationsDetailScreenPreview(){
    NewTrackMedTheme {
        Column(modifier = Modifier.padding(16.dp)) {

        MedicationDetailsNameStrengthCard(medication = previewMedicationEntities[0]) {
            
        }
            
        }
    }
}