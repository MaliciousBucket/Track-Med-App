package com.example.newtrackmed.ui.feature.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(

){
    val reportsViewModel: ReportsViewModel = viewModel(
        factory = ReportsViewModel.Factory
    )

    val uiState by reportsViewModel.uiState.collectAsStateWithLifecycle()

    val allDonutData by reportsViewModel.allDoseDonutChartData.collectAsStateWithLifecycle()


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
    ){innerPadding ->




    Column (
        modifier = Modifier
            .padding(innerPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { reportsViewModel.showTestChart() }) {
                Text(text = "Show Test Chart")
            }

            Button(onClick = { reportsViewModel.donutForId() }) {

                Text(text = "Get Pie Chart of med 0")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(onClick = { reportsViewModel.recentDetailDosesForMed() }) {
                Text(text = "Get recent for Id 0")
            }

            Button(onClick = { reportsViewModel.recentDetailDosesForMultipleMeds() }) {
                Text(text = "Get Recentfor  0 and 1")
            }


        }

        when (uiState.recentDosesWithDetails) {
            is RecentDosesWithDetails.Success -> {
                DisplayRecentDoses(
                    recentDoses = (uiState.recentDosesWithDetails as RecentDosesWithDetails.Success).recentDoses
                ) {}
            }

            is RecentDosesWithDetails.Loading -> {
                Text(text = "Loading XDDD")
            }
        }

        when (uiState.donutChartStatus) {
            is ChartStatus.Donut -> {
                NewDonut(pieChartData = allDonutData) {
                    reportsViewModel.fetchAllDoseDonutChartData()
                }
            }

            is ChartStatus.Loading -> {
                Text(text = "Loading")
            }

            is ChartStatus.Error -> {
                Text(text = "Error")
            }
            else -> {
                Text(text = "What is this man?")
            }
        }


    }
    }

}