package com.example.newtrackmed.ui.feature.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newtrackmed.R
import com.example.newtrackmed.data.model.DoseChipStatus
import com.example.newtrackmed.data.model.DoseViewData
import com.example.newtrackmed.ui.component.BottomNavBar
import com.example.newtrackmed.ui.component.ErrorScreenDisplay
import com.example.newtrackmed.ui.component.LoadingScreenDisplay

@Composable
fun HomeScreen(){
    val homeViewModel: HomeScreenViewModel = viewModel(
        factory = HomeScreenViewModel.Factory
    )
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold (
        topBar = {
            HomeTopAppBar(
                totalDailyDosesCount = 8,
                dailyDoseProgress =5,
                onAddDoseMedClicked = { /*TODO*/ },
                onAddMedClicked = { homeViewModel.onNextDateClicked() },
                onAddDoseClicked = { /*TODO*/ },
                onCalendarClicked = { /*TODO*/ },
                menuExpanded = false
            ) {

            }

        },
        bottomBar = {
            BottomNavBar(
                onHomeClick = { /*TODO*/ },
                onMyMedicationsClick = { /*TODO*/ },
                onReportsClick = {}
            )
        },
    )
    { innerPadding ->


        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            DateIndicator(
                selectedDate = uiState.selectedDate,
                onPreviousClick = { homeViewModel.onPreviousDateClicked() },
                onNextClick = {homeViewModel.onNextDateClicked()},
                onDateClicked = {homeViewModel.onDateChange(it)},
            )

            val modifier = Modifier.padding(innerPadding)
            when (uiState.viewData) {
                is DoseDisplayUIState.Success -> {
                    DisplayDoseCards(
//                        modifier = modifier,
                        viewData = (uiState.viewData as DoseDisplayUIState.Success).doseViewData
                    )
                    { medicationId, doseId ->
                        homeViewModel.onCardClicked(medicationId, doseId)
                    }

                }

                DoseDisplayUIState.Loading -> {
                    LoadingScreenDisplay(title = R.string.home_screen_loading_message)
                }

                DoseDisplayUIState.Error -> {
                    ErrorScreenDisplay(title = R.string.loading_daily_doses_error)
                }
                else -> {}
            }
            when (uiState.dialogUIState) {
                is UpdateDoseDialogUIState.Success -> {
                    (uiState.dialogUIState as UpdateDoseDialogUIState.Success).updateDoseData?.let {
                        UpdateDoseDialog(
                            updateDoseData = it,
                            onDismissRequest = { homeViewModel.onCancelDialogClicked()},
                            onTakeClick = { homeViewModel.onTakeClicked() },
                            onUnTakeClick = { homeViewModel.onUnTakeClicked() },
                            onSkipClick = { homeViewModel.onSkippedClicked() },
                            onMissedClick = { homeViewModel.onMissedClicked() },
                            onTakeNowClick = {homeViewModel.onTakeNowClicked()},
                            onCancelClick = { homeViewModel.onCancelDialogClicked() },
                            onEditClick = { /*TODO*/ }) {

                        }
                    }
                }
                is UpdateDoseDialogUIState.Hidden -> {
                }
            }
        }
    }


}

//TODO: Check recent dose item for layout
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoseCard(
    viewData: DoseViewData,
    onCardClicked: () -> Unit,
){
    Log.d("Debug Dose Card", "Recieved: $viewData")
    val cardWidth = 360.dp
    val cardHeight = 72.dp

    OutlinedCard(
        modifier = Modifier
            .size(cardWidth, cardHeight)
            .border(2.dp, Color.Black, RoundedCornerShape(8.dp)),
        onClick = { onCardClicked() }
    ){
        Row(
            Modifier
                .padding(start = 16.dp, top = 8.dp, end = 10.dp, bottom = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ){
            DoseCardMedDetails(name = viewData.name,
                dosage = viewData.dosage,
                dosageUnit = viewData.dosageUnit,
                unitsTaken = viewData.unitsTaken,
                type = viewData.type)
            Spacer(
                Modifier
                    .weight(1f)
                    .height(32.dp)
            )
            DoseStatusChip(chipStatus = viewData.chipStatus)
        }
    }
}

@Composable
fun DoseCardMedDetails(name: String, dosage: Int, dosageUnit: String,
                       unitsTaken: Int, type: String){
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(text = name)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = dosage.toString() + "" + dosageUnit)
            HorizontalDivider(modifier = Modifier
                .padding(0.dp)
                .width(1.dp)
                .height(20.dp)
                .background(color = Color(0xFFCAC4D0))
            )
            Text(text = "$unitsTaken $type(s)")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoseStatusChip(
    chipStatus: DoseChipStatus
){
    FilterChip(
        selected =true,
        onClick = { /*TODO*/ },
        label = { Text(text = stringResource(id = chipStatus.stringValue)) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = chipStatus.chipColor,
            selectedLabelColor = chipStatus.chipLabelColor
        ),
        leadingIcon = {
            Icon(
                tint = chipStatus.chipLabelColor,
                imageVector = chipStatus.icon,
                contentDescription = null,

                )
        }
    )
}

@Composable
fun DisplayDoseCards(
    modifier: Modifier = Modifier,
    viewData: List<DoseViewData>,
    onCardClicked: (Int, Int?) -> Unit
){
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        itemsIndexed(viewData){ index, item ->
            DoseCard(viewData = item) {
                onCardClicked(item.medicationId, item.doseId)
            }
        }
    }
}

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