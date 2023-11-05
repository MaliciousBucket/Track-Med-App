package com.example.newtrackmed.ui.feature.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.PieChartOutline
import androidx.compose.material.icons.outlined.StackedLineChart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.common.extensions.isNotNull
import com.example.newtrackmed.R
import com.example.newtrackmed.ui.component.BottomNavBar
import com.example.newtrackmed.ui.feature.addmedication.MedQuestionListOption
import com.example.newtrackmed.ui.feature.addmedication.MedQuestionNavOption
import com.example.newtrackmed.ui.feature.mymedications.DisplayMyMedicationsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewReportsScreen(
    onNavToHome: () -> Unit,
    onNaveToMyMedications: () -> Unit
){
    val reportsViewModel : NewReportsViewModel = viewModel(
        factory = NewReportsViewModel.Factory
    )

    val chartData = reportsViewModel.donutChartData
    val graphState by reportsViewModel.graphState.collectAsStateWithLifecycle()
    val myMedsData = reportsViewModel.allMedsViewData
    val screenState by reportsViewModel.screenState.collectAsStateWithLifecycle()
    val modelProducer = reportsViewModel.barChartBuilder

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(

                title = { Text(text = "Reports") },
                actions = {
                    ReportsDropDownMenu(
                        onPieChartClick = { reportsViewModel.onSetToDonutClicked() },
                        onBarChartClick = { reportsViewModel.onSetToBarClicked() },
                        onLineChartClick = {reportsViewModel.onSetToLineClicked()}
                        )


                },
                navigationIcon = {
                    IconButton(onClick = { reportsViewModel.setOverview() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )

        },

        bottomBar = {
            BottomNavBar(
                onHomeClick = { onNavToHome() },
                onMyMedicationsClick = { onNaveToMyMedications() },
                onReportsClick = {}
            )


//            OutlinedCard(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Text(text = "Bottom of the screen!")
//                }
//            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            when (screenState) {
                is ReportScreenState.Overview -> {
                    if (myMedsData.isNotNull()){
                        MedQuestionNavOption(
                            title = R.string.all_medications,
                            isSelected = false,
                            onOptionClicked = { reportsViewModel.displayAllMedReports() }
                        )
                        DisplayMyMedicationsList(
                            myMedicationViewDataList = myMedsData,
                            onListItemClick = {reportsViewModel.displayMedReports(it)})
                    }
                }
                is ReportScreenState.AllMedReports -> {
                        when (graphState) {
                            is GraphState.Donut -> {
                                    NewDonut(pieChartData = chartData)
                            }
                            is GraphState.BarChart -> {
                                BigBadChart(
                                    modelProducer = modelProducer,
                                    valueFormatter = reportsViewModel.bottomAxisFormatter
                                )
                            }
                            is GraphState.LineChart -> {
                                TestLineChart(modelProducer = reportsViewModel.lineCHartBuilder)
                            }
                            else -> {
                                Text(text = "No Donut")
                            }
                        }
                        val selectedIndex = reportsViewModel.selectedMedsIndex
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            itemsIndexed(myMedsData) { index, item ->
                                MedQuestionListOption(
                                    text = item.name,
                                    isSelected = item.medicationId in selectedIndex,
                                    onOptionSelected = { reportsViewModel.onMedItemSelected(item.medicationId) })

                            }
                        }
                }
                is ReportScreenState.DetailReport -> {
                    when (graphState) {
                        is GraphState.Donut -> {
                            NewDonut(pieChartData = chartData)
                        }
                        is GraphState.BarChart -> {
                            BigBadChart(
                                modelProducer = modelProducer,
                                valueFormatter = reportsViewModel.bottomAxisFormatter
                            )
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

        }
    }

}

@Composable
fun ReportsDropDownMenu(
    onPieChartClick: () -> Unit,
    onBarChartClick: () -> Unit,
    onLineChartClick: () -> Unit,
){
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
//            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Open Menu")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Donut Chart") },
                onClick = { onPieChartClick() },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.PieChartOutline,
                        contentDescription = null
                    )
                })
            DropdownMenuItem(
                text = { Text("Bar Chart") },
                onClick = { onBarChartClick() },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.BarChart,
                        contentDescription = null
                    )
                })
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Dose Time") },
                onClick = { onLineChartClick() },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.StackedLineChart,
                        contentDescription = null
                    )
                })
        }
    }
}