package com.example.newtrackmed.ui.feature.report

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.newtrackmed.data.entity.DoseStatus
import com.example.newtrackmed.data.model.DoseCount
import com.example.newtrackmed.data.repository.DoseRepository
import com.example.newtrackmed.di.TrackMedApp
import com.example.newtrackmed.ui.feature.mymedications.MyMedicationsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Flow

class DonutChartViewModel(
    private val doseRepository: DoseRepository
): ViewModel() {


    private val _pieChartData = MutableStateFlow<PieChartData?>(null)
    val pieChartData = _pieChartData.asStateFlow()

    private val statusColours = mapOf(
        DoseStatus.TAKEN to Color(0xFF00FF00),
        DoseStatus.MISSED to Color(0xFFFF0000),
        DoseStatus.SKIPPED to Color(0xFFFFFF00),
        DoseStatus.RESCHEDULED to Color(0xFFFFA500)
    )

    fun fetchDoseDonutChartData(){
        viewModelScope.launch {
            doseRepository.getDoseCounts().first().let {doseCounts ->
                val slices = doseCounts.map { doseCount ->
                    val colour = statusColours[doseCount.status] ?: Color.Black
                    PieChartData.Slice(
                        doseCount.status.name,
                        doseCount.count.toFloat(),
                        colour)
                }

                val newPieChartData = PieChartData(
                    slices = slices,
                    plotType = PlotType.Donut
                )
                _pieChartData.update { newPieChartData }
            }
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appModule = TrackMedApp.appModule
                val doseRepository = appModule.doseRepository

                DonutChartViewModel(
                    doseRepository = doseRepository
                )

            }
        }
    }



}