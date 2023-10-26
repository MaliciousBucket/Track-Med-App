package com.example.newtrackmed.ui.feature.mymedications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newtrackmed.data.doseViewDataList
import com.example.newtrackmed.data.entity.FrequencyType
import com.example.newtrackmed.data.model.DoseViewData
import com.example.newtrackmed.data.model.RecentDoseDetails
import com.example.newtrackmed.data.previewUpdateDoseData
import com.example.newtrackmed.data.recentDosePreviewList
import com.example.newtrackmed.ui.feature.home.DisplayDoseCards
import com.example.newtrackmed.ui.feature.home.DoseStatusChip
import com.example.newtrackmed.ui.theme.NewTrackMedTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import java.util.jar.Attributes.Name

@Composable
fun MyMedicationsDetailScreen(){

}

@Composable
fun MedicationDetailsNameStrengthCard(){

}

@Composable
fun MedicationDetailsFrequencyCard(
    frequencyType: FrequencyType,
    asNeeded: Boolean,
    interval: String,
    startDate: LocalDate,
    endDate: LocalDate,
    timeToTake: LocalTime,
    dosage: Int,
    dosageUnit: String,
    unitsTaken: Int,
    type: String
    //TODO: Add the on click events

){
    Column(
        
    ) {
        Text(
            text = "Frequency",
            style = MaterialTheme.typography.titleMedium
        )
        Column(
            
        ) {
            OutlinedCard(
                shape = RoundedCornerShape(0.dp),
            ) {
                Column(

                ) {
                   MedicationDetailsFrequencyDetails(
                       frequencyType = frequencyType,
                       asNeeded = asNeeded,
                       interval = interval)
                    }
            }
            OutlinedCard(
                shape = RoundedCornerShape(0.dp),
            ) {
                FrequencyTimeDetails(
                    timeToTake = timeToTake,
                    dosage = dosage,
                    dosageUnit = dosageUnit,
                    unitsTaken = unitsTaken,
                    type = type
                )
            }
            MedicationDetailsStartEndDates(
                startDate = startDate,
                endDate = endDate
            )

            MedDetailsAsNeededSwitch(asNeeded = asNeeded) {
                
            }
        }
    }
}

@Composable
fun MedicationDetailsFrequencyDetails(
    frequencyType: FrequencyType,
    asNeeded: Boolean,
    interval: String
){
    val (primaryText, secondaryText) = getFrequencyTypeText(frequencyType, asNeeded, interval)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
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
}

//TODO: Refactor to use string resources
fun getFrequencyTypeText(type: FrequencyType, asNeeded: Boolean, interval: String) : Pair<String, String>{
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

@Composable
fun FrequencyTimeDetails(
    timeToTake: LocalTime,
    dosage: Int,
    dosageUnit: String,
    unitsTaken: Int,
    type: String
){
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    val formattedTime = timeToTake.format(formatter)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = formattedTime,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Take $unitsTaken $dosage$dosageUnit $type(s)",
            style = MaterialTheme.typography.titleMedium
        )
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
fun MedicationDetailsStartEndDates(
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                modifier = Modifier.padding(2.dp),
                text = "Start Date: $formattedStartDateSimple",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "You started taking this medication on: $formattedStartDate",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.padding(2.dp),
                text = "End Date: $formattedEndDateSimple",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "You are due to stop taking this medication on: $formattedEndDate",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentDoseItem(
    recentDose: RecentDoseDetails,
    onClick: (Int) -> Unit,
){
    OutlinedCard(
        modifier =Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        onClick = { onClick(recentDose.doseId)}
    ) {
        Row(
            Modifier
                .padding(start = 16.dp, top = 8.dp, end = 10.dp, bottom = 8.dp)
                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){
            RecentDoseItemDetails(
                modifier = Modifier.fillMaxWidth(),
                name = recentDose.name,
                dosage = recentDose.dosage,
                type = recentDose.type,
                takenTime = recentDose.doseTime
            )

            DoseStatusChip(chipStatus = recentDose.chipStatus)
        }
    }
}

@Composable
fun RecentDoseItemDetails(
    modifier: Modifier,
    name: String,
    dosage: Int,
    type: String,
    takenTime: LocalDateTime

){
    val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yy")
    val formattedTime = takenTime.format(formatter)
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
    ){
        Text(text = formattedTime)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = name)
            HorizontalDivider(modifier = Modifier
                .padding(0.dp)
                .width(1.dp)
                .height(20.dp)
                .background(color = Color(0xFFCAC4D0))
            )
            Text(text = "$dosage $type(s)")
        }
    }
}

@Composable
fun MedDetailsAsNeededSwitch(
    asNeeded: Boolean,
    onCheckChange: () -> Unit
){
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
                style = MaterialTheme.typography.headlineSmall
                )
            Switch(
                checked = asNeeded,
                onCheckedChange = {
                    onCheckChange()
                },
                thumbContent = if (asNeeded) {
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



@Composable
fun DosesForTodayView(
    onCardClicked: () -> Unit,
    viewData: List<DoseViewData>
){
    RecentDosesExpandableCard(title = "Doses For Today") {
        if(viewData.isNotEmpty()) {
            DisplayDoseCards(viewData = viewData) {
                onCardClicked()
            }
        } else {
            Text(text = "No doses for today!")
        }
    }

}

@Composable
fun DosesTakenForToday(
    recentDoses: List<RecentDoseDetails>,
    onCardClicked: (Int) -> Unit
){
    if(recentDoses.isNotEmpty()){
        RecentDosesExpandableCard(title = "Doses Taken Today") {
            DisplayRecentDoses(recentDoses = recentDoses, onCardClicked = onCardClicked)
      
        }
    }
}

@Composable
fun DisplayRecentDoses(
    recentDoses: List<RecentDoseDetails>,
    onCardClicked: (Int) -> Unit
) {
    LazyColumn {
        itemsIndexed(
            recentDoses,
            key = { _, item ->
                item.doseId
            }
        ) { _, item ->
            RecentDoseItem(recentDose = item, onClick = onCardClicked)
        }
    }
}

@Composable
fun RecentDosesExpandableCard(
    title: String,
    content: @Composable () -> Unit,

) {
    var expanded by remember { mutableStateOf(true) }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }

    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(24.dp)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                    contentDescription = "Expand/Collapse Arrow",
                    modifier = Modifier
                        .padding(4.dp)
                        .graphicsLayer(rotationZ = if (expanded) 90f else 0f)
                )
            }
            if (expanded) {
                content()
            }
        }
        HorizontalDivider()
    }
}
//}



@Composable
fun UpcomingDosesForToday(

){

}

@Composable
fun RecentDosesView(

){

}

@Composable
fun RecentDosesViewContainer(

){

}

@Preview(showBackground = true)
@Composable
fun MyMedicationsDetailScreenPreview(){



    NewTrackMedTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            MedDetailsAsNeededSwitch(true, {})
            Spacer(modifier = Modifier.height(16.dp))
            MedicationDetailsFrequencyDetails(
                frequencyType = FrequencyType.WEEK_DAYS,
                asNeeded = false,
                interval = "Monday, Tuesday, Friday"
            )

            MedicationDetailsFrequencyCard(
                frequencyType = FrequencyType.WEEK_DAYS,
                asNeeded = false,
                interval = "Monday, Tuesday, Friday",
                startDate = LocalDate.ofYearDay(2023, 186),
                endDate = LocalDate.now().plusDays(6),
                timeToTake = LocalTime.of(12, 20),
                dosage = 50,
                dosageUnit = "mg",
                unitsTaken = 2,
                type = "pill",
            )

//            DisplayRecentDoses(recentDoses = recentDosePreviewList, onCardClicked = {} )
            DosesTakenForToday(recentDoses = recentDosePreviewList, onCardClicked = {})
            Spacer(modifier = Modifier.height(16.dp))

            DosesForTodayView(onCardClicked = {  }, viewData = doseViewDataList)


        }
    }
}