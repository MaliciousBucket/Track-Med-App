package com.example.newtrackmed.ui.feature.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.common.extensions.isNotNull
import com.example.newtrackmed.ui.feature.addmedication.MedQuestionListOption
import com.example.newtrackmed.ui.feature.mymedications.DisplayMyMedicationsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewReportsScreen(){
    val reportsViewModel : NewReportsViewModel = viewModel(
        factory = NewReportsViewModel.Factory
    )

//    val uiState = reportsViewModel.uiState

    val chartData = reportsViewModel.donutChartData
    val graphState by reportsViewModel.graphState.collectAsStateWithLifecycle()
    val myMedsData = reportsViewModel.allMedsViewData
    val screenState by reportsViewModel.screenState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Reports") })

        },

        bottomBar = {
            OutlinedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Bottom of the screen!")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (screenState) {
                is ReportScreenState.Overview -> {
                    if (myMedsData.isNotNull()){
                        Button(onClick = { reportsViewModel.displayAllMedReports() }) {
                            Text(text = "All Meds")
                        }
                        DisplayMyMedicationsList(
                            myMedicationViewDataList = myMedsData,
                            onListItemClick = {reportsViewModel.displayMedReports(it)})
                    }
                }

                is ReportScreenState.AllMedReports -> {
                    Button(onClick = { reportsViewModel.onRefreshClicked() }) {
                        Text(text = "Refresh Chart")
                    }
                    val selectedIndex = reportsViewModel.selectedMedsIndex
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(myMedsData) { index, item ->
                            MedQuestionListOption(
                                text = item.name,
                                isSelected = item.medicationId in selectedIndex,
                                onOptionSelected = { reportsViewModel.onMedItemSelected(item.medicationId)})
                        }
                    }

                    when (graphState) {
                        is GraphState.Donut -> {
                            NewDonut(pieChartData = chartData)
                        }
                        else -> {
                            Text(text = "No Donut")
                        }
                    }
                }

                is ReportScreenState.DetailReport -> {
                    when (graphState) {
                        is GraphState.Donut -> {
                            NewDonut(pieChartData = chartData)
                        }
                        else -> {
                            Text(text = "No Donut")
                        }
                    }
                }

                is ReportScreenState.Loading -> {
                    Text(text = "Loading..")
                }
            }
            Button(onClick = { reportsViewModel.setOverview()}) {
                Text(text = "Try Overview")
            }

            Button(onClick = { reportsViewModel.setDonut()}) {
                Text(text = "Setup Donut")
            }
            Button(onClick = { reportsViewModel.setDonutById(1) }) {
                Text(text = "Set Donut 1")
            }
            Row {
                Button(onClick = { reportsViewModel.setDonutById(2) }) {
                    Text(text = "Set Donut 2")
                }
                val list = listOf<Int>(1,2,3,4)
                Button(onClick = { reportsViewModel.setListDonut(list) }) {
                    Text(text = "SetList")
                }


            }



        }
    }

}