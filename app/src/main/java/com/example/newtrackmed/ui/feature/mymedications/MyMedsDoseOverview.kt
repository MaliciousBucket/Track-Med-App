package com.example.newtrackmed.ui.feature.mymedications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.unit.dp
import com.example.newtrackmed.data.model.DoseViewData
import com.example.newtrackmed.data.model.RecentDoseDetails
import com.example.newtrackmed.ui.feature.home.DisplayDoseCards
import com.example.newtrackmed.ui.feature.home.DoseStatusChip
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DosesForTodayView(
    onCardClicked: (Int, Int?) -> Unit,
    viewData: List<DoseViewData>
){
    RecentDosesExpandableCard(title = "Doses For Today") {
        if(viewData.isNotEmpty()) {
            DisplayDoseCards(
                viewData = viewData,
                onCardClicked = onCardClicked
            )

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