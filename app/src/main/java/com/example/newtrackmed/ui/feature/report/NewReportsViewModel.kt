package com.example.newtrackmed.ui.feature.report

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import co.yml.charts.common.extensions.isNotNull
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.newtrackmed.data.entity.DoseStatus
import com.example.newtrackmed.data.entity.MedicationEntity
import com.example.newtrackmed.data.model.LastTakenDose
import com.example.newtrackmed.data.model.MyMedicationsViewData
import com.example.newtrackmed.data.model.RecentDoseDetails
import com.example.newtrackmed.data.model.mapToMyMedicationsViewData
import com.example.newtrackmed.data.repository.DoseRepository
import com.example.newtrackmed.data.repository.MedicationRepository
import com.example.newtrackmed.di.TrackMedApp
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class NewReportUiState(
    val donutData: PieChartData?,
    val recentDoses: List<RecentDoseDetails?>,
    val graphState: GraphState
)

@Immutable
sealed interface GraphState{
    object Donut : GraphState
    object BarChart : GraphState
    object LineChart : GraphState
    object RecentDoses : GraphState
    object Loading: GraphState
    object EmptyData: GraphState
}

@Immutable
sealed interface ReportScreenState{
    object Loading: ReportScreenState
    object Overview: ReportScreenState
    object AllMedReports: ReportScreenState
    object DetailReport: ReportScreenState
}

data class ReportsListItem(
    val name: String,
    val id: Int,
)

class NewReportsViewModel(
    private val doseRepository: DoseRepository,
    private val medicationRepository: MedicationRepository
): ViewModel() {

    private val _donutChartData = mutableStateOf<PieChartData?>(null)

    val donutChartData: PieChartData?
        get() = _donutChartData.value

    private val _recentDoseData = mutableStateListOf<RecentDoseDetails?>()
    val recentDoseData: List<RecentDoseDetails?>
        get() = _recentDoseData


    private val _graphState = MutableStateFlow<GraphState>(GraphState.Loading)

    val graphState = _graphState.asStateFlow()

    private val _screenState = MutableStateFlow<ReportScreenState>(ReportScreenState.Loading)
    val screenState = _screenState.asStateFlow()




    private val statusColours = mapOf(
        DoseStatus.TAKEN to Color(0xFF00FF00),
        DoseStatus.MISSED to Color(0xFFFF0000),
        DoseStatus.SKIPPED to Color(0xFFFFFF00),
        DoseStatus.RESCHEDULED to Color(0xFFFFA500)
    )

    private val _allMedications = mutableListOf<MedicationEntity>()
    val allMedications: List<MedicationEntity>
        get() = _allMedications

    private val _lastDosesForAllMeds = mutableListOf<LastTakenDose>()

    private val _allMedsViewData = mutableStateListOf<MyMedicationsViewData>()
    val allMedsViewData
        get() = _allMedsViewData.toList()

    private val _displayMedicationId = MutableStateFlow(1)

    private val _displayMedItems= mutableStateListOf<ReportsListItem>()

    val displayMedItems: List<ReportsListItem>
        get() = _displayMedItems


    private val _selectedMedsIndex = mutableStateListOf<Int>()
    val selectedMedsIndex : List<Int>
        get() = _selectedMedsIndex

    fun onMedItemSelected(medicationId: Int){
        if(medicationId in _selectedMedsIndex) {
            _selectedMedsIndex.remove(medicationId)
        }else {
            _selectedMedsIndex.add(medicationId)
        }
    }

    fun onRefreshClicked(){
        viewModelScope.launch {
        if (_selectedMedsIndex.isNotEmpty()) {
            setListDonut(_selectedMedsIndex)
        }
            else {
                _graphState.update { GraphState.EmptyData }
        }

        }
    }



    init {
        setupData()
    }



    private fun setupData(){
        _graphState.update { GraphState.Loading }
        _screenState.update { ReportScreenState.Loading }
        viewModelScope.launch {

            val allMedsDeferred = async { medicationRepository.getAllSuspendMedications() }
            val allLastTakenDeferred = async { doseRepository.getLastTakenDosesForAllMeds() }

            val allMeds = allMedsDeferred.await()
            val lastTaken = allLastTakenDeferred.await()

            _allMedications.clear()
            _allMedications.addAll(allMeds)

            _lastDosesForAllMeds.clear()
            _lastDosesForAllMeds.addAll(lastTaken)



            _allMedsViewData.clear()
            _allMedsViewData.addAll(
                _allMedications.map { medication ->
                    val lastDose = _lastDosesForAllMeds.find { it.medicationId == medication.id }
                    medication.mapToMyMedicationsViewData(lastDose)
                }
            )
            _screenState.update { ReportScreenState.Overview }
        }
    }

    fun setOverview(){
        _screenState.update { ReportScreenState.Overview }
    }

    fun displayMedReports(medicationId: Int){
        _screenState.update { ReportScreenState.Loading }
        _graphState.update { GraphState.Loading }
        viewModelScope.launch {
            _displayMedicationId.update { medicationId }
            setDonutById(_displayMedicationId.value)
            _screenState.update { ReportScreenState.DetailReport }
        }
    }

    fun displayAllMedReports(){
        _screenState.update { ReportScreenState.Loading }
        _graphState.update { GraphState.Loading }
        viewModelScope.launch{
            setDonut()
            _screenState.update { ReportScreenState.AllMedReports }
        }
    }



    fun setDonut() {
        _graphState.update { GraphState.Loading }
        viewModelScope.launch {
            val doseCounts = doseRepository.getSuspendDoseCounts()
            Log.d("Debug test Donut", "DoseCounts: $doseCounts")
            val slices = doseCounts.map { doseCount ->
                val colour = statusColours[doseCount.status] ?: Color.Black
                PieChartData.Slice(
                    doseCount.status.name,
                    doseCount.count.toFloat(),
                    colour
                )
            }
            val newPieChartData = PieChartData(
                slices = slices,
                plotType = PlotType.Donut
            )

            if (newPieChartData.isNotNull()) {
                _donutChartData.value = newPieChartData
                _graphState.update { GraphState.Donut }

            } else {
                Log.d("Donut Data", "Entered Null. $_donutChartData")
                _graphState.update { GraphState.EmptyData }

            }
        }
    }

    fun setDonutById(medicationId: Int){
        _graphState.update { GraphState.Loading }
        viewModelScope.launch {
            val doseCounts = doseRepository.getSuspendDoseCountsByMedId(medicationId)
            Log.d("Debug test Donut", "DoseCounts: $doseCounts")
            val slices = doseCounts.map { doseCount ->
                val colour = statusColours[doseCount.status] ?: Color.Black
                PieChartData.Slice(
                    doseCount.status.name,
                    doseCount.count.toFloat(),
                    colour
                )
            }
            val newPieChartData = PieChartData(
                slices = slices,
                plotType = PlotType.Donut
            )
            Log.d("Debug Chart Data", "Pie Data $newPieChartData")

            if (newPieChartData.isNotNull()) {
                _donutChartData.value = newPieChartData
                _graphState.update { GraphState.Donut }

            } else {
                Log.d("Donut Data", "Entered Null. $_donutChartData")
                _graphState.update { GraphState.EmptyData }

            }
        }
    }

    fun setListDonut(medicationIds: List<Int>) {
        _graphState.update { GraphState.Loading }
        viewModelScope.launch {
            val doseCounts = doseRepository.getSuspendDoseCountsByMultipleMedIds(medicationIds)
            Log.d("Debug test Donut", "DoseCounts: $doseCounts")
            val slices = doseCounts.map { doseCount ->
                val colour = statusColours[doseCount.status] ?: Color.Black
                PieChartData.Slice(
                    doseCount.status.name,
                    doseCount.count.toFloat(),
                    colour
                )
            }
            val newPieChartData = PieChartData(
                slices = slices,
                plotType = PlotType.Donut
            )
            Log.d("Debug Chart Data", "Pie Data $newPieChartData")

            if (newPieChartData.isNotNull()) {
                _donutChartData.value = newPieChartData
                _graphState.update { GraphState.Donut }

            } else {
                Log.d("Donut Data", "Entered Null. $_donutChartData")
                _graphState.update { GraphState.EmptyData }

            }
        }
    }



    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appModule = TrackMedApp.appModule
                val doseRepository = appModule.doseRepository
                val medicationRepository = appModule.medicationRepository

                NewReportsViewModel(
                    doseRepository = doseRepository,
                    medicationRepository = medicationRepository
                )

            }
        }
    }

}