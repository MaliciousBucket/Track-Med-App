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
import com.example.newtrackmed.data.model.DoseCountWithId
import com.example.newtrackmed.data.model.LastTakenDose
import com.example.newtrackmed.data.model.MyMedicationsViewData
import com.example.newtrackmed.data.model.RecentDoseDetails
import com.example.newtrackmed.data.model.mapToMyMedicationsViewData
import com.example.newtrackmed.data.repository.DoseRepository
import com.example.newtrackmed.data.repository.MedicationRepository
import com.example.newtrackmed.di.TrackMedApp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
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

enum class GraphType{
    DONUT,
    BAR,
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


//    private val _selectedMedsIndex = mutableSetOf<Int>()
private val _selectedMedsIndex = mutableStateListOf<Int>()
    val selectedMedsIndex : List<Int>
        get() = _selectedMedsIndex.toList()

    private val _doseDataWithIds = mutableStateListOf<DoseCountWithId>()

    private val _chartBuilder = ChartEntryModelProducer()

    val chartBuilder : ChartEntryModelProducer
        get() = _chartBuilder

    private val _currentGraphType = mutableStateOf<GraphType>(GraphType.DONUT)

    private val selectedMedNamesState = mutableStateListOf<String>()



    private val _bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ ->
            selectedMedNamesState.getOrNull(x.toInt()) ?: ""
        }

    val bottomAxisFormatter : AxisValueFormatter<AxisPosition.Horizontal.Bottom>
        get() = _bottomAxisValueFormatter


    fun updateTestChartEntries() {
        viewModelScope.launch {
            if (_selectedMedsIndex.isNotEmpty()) {
                val takenEntries = mutableListOf<ChartEntry>()
                val missedEntries = mutableListOf<ChartEntry>()
                val skippedEntries = mutableListOf<ChartEntry>()
                val rescheduledEntries = mutableListOf<ChartEntry>()

                _selectedMedsIndex.forEachIndexed { index, medicationId ->
                    val medicationDoseCounts = _doseDataWithIds.filter { it.medicationId == medicationId }


                    takenEntries.add(entryOf(index.toFloat(), medicationDoseCounts.find { it.status == DoseStatus.TAKEN }?.count?.toFloat() ?: 0f))
                    missedEntries.add(entryOf(index.toFloat(), medicationDoseCounts.find { it.status == DoseStatus.MISSED }?.count?.toFloat() ?: 0f))
                    skippedEntries.add(entryOf(index.toFloat(), medicationDoseCounts.find { it.status == DoseStatus.SKIPPED }?.count?.toFloat() ?: 0f))
                    rescheduledEntries.add(entryOf(index.toFloat(), medicationDoseCounts.find { it.status == DoseStatus.RESCHEDULED }?.count?.toFloat() ?: 0f))
                }

                val groupedEntries = listOf(takenEntries, missedEntries, skippedEntries, rescheduledEntries)

                _chartBuilder.setEntries(groupedEntries)
                _graphState.value = GraphState.BarChart
            } else {
                _graphState.value = GraphState.EmptyData
            }
        }
    }

    private fun updateChartEntries() {
        viewModelScope.launch {
            if (_selectedMedsIndex.isNotEmpty()) {
                Log.d("Not Empty", "List: $_selectedMedsIndex")
                val filteredDoseCounts = _doseDataWithIds.filter { it.medicationId in _selectedMedsIndex }

                val entries = filteredDoseCounts.groupBy { it.status }
                    .flatMap { (status, counts) ->
                        counts.map { doseCount ->
                            entryOf(status.ordinal, doseCount.count.toFloat())
                        }
                    }


                _chartBuilder.setEntries(entries)

                _graphState.value = GraphState.BarChart
            } else {
                Log.d("Setting Graph State", "Empty. List: $_selectedMedsIndex")
//                _chartBuilder.setEntries()
                _graphState.value = GraphState.EmptyData
            }
        }
    }





    fun onMedItemSelected(medicationId: Int){
        val medicationName = _allMedications.find { it.id == medicationId }?.name ?: return
        Log.d("Selected", "Medication ID: $medicationId")

        // Toggle the selection state and add/remove the name
        //There are better ways of doing this...
        if (!_selectedMedsIndex.remove(medicationId)) {
            _selectedMedsIndex.add(medicationId)
            selectedMedNamesState.add(medicationName)
            Log.d("Added", "Added $medicationName with ID $medicationId")
        } else {
            selectedMedNamesState.remove(medicationName)
            Log.d("Removed", "Removed $medicationName with ID $medicationId")
        }

        // Update the graph state
        //Null checks, otherwise this crashes :(
        if(_selectedMedsIndex.isEmpty()){
            Log.d("Empty", "No selected medications.")
            _graphState.update { GraphState.EmptyData }
        } else {
            Log.d("Refreshing", "Selected medications: $_selectedMedsIndex")
            refreshGraphData()
        }
    }

    private fun refreshGraphData() {
        viewModelScope.launch {
            when (_currentGraphType.value) {
                GraphType.DONUT -> {
                    setDoseDonut()
                }
                GraphType.BAR -> {
                    updateTestChartEntries()
                }

            }
        }
    }

    fun switchGraphType(newType: GraphType) {
        _currentGraphType.value = newType
        refreshGraphData()
    }



    fun onSetToDonutClicked(){
        switchGraphType(GraphType.DONUT)
    }

    fun onSetToBarClicked(){
        switchGraphType(GraphType.BAR)
    }


//    fun onRefreshClicked(){
//        viewModelScope.launch {
//        if (_selectedMedsIndex.isNotEmpty()) {
//            setListDonut(_selectedMedsIndex.toList())
//        }
//            else {
//                _graphState.update { GraphState.EmptyData }
//        }
//
//        }
//    }



    init {
        setupData()
    }



    private fun setupData(){
        _graphState.update { GraphState.Loading }
        _screenState.update { ReportScreenState.Loading }
        viewModelScope.launch {

            val allMedsDeferred = async { medicationRepository.getAllSuspendMedications() }
            val allLastTakenDeferred = async { doseRepository.getLastTakenDosesForAllMeds() }
            val doseCountsDeferred = async { doseRepository.getSuspendDoseCountWithIdByStatus() }

            val allMeds = allMedsDeferred.await()
            val lastTaken = allLastTakenDeferred.await()
            val doseCounts = doseCountsDeferred.await()

            _allMedications.clear()
            _allMedications.addAll(allMeds)

            _lastDosesForAllMeds.clear()
            _lastDosesForAllMeds.addAll(lastTaken)

            _doseDataWithIds.clear()
            _doseDataWithIds.addAll(doseCounts)

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
        viewModelScope.launch {
            _screenState.update { ReportScreenState.Loading }
            _screenState.update { ReportScreenState.Overview }
        }

    }

    fun displayMedReports(medicationId: Int){
        _screenState.update { ReportScreenState.Loading }
        _graphState.update { GraphState.Loading }
        viewModelScope.launch {
            _displayMedicationId.update { medicationId }
            _selectedMedsIndex.clear()
            selectedMedNamesState.clear()

            onMedItemSelected(medicationId)
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

    private fun setDoseDonut(){
        _graphState.update { GraphState.Loading }
        viewModelScope.launch {
            // Filter the dose counts by the selected medication IDs
            val doseCounts = _doseDataWithIds.filter { it.medicationId in _selectedMedsIndex }

            // Map the data to slices
            val slices = doseCounts.groupBy { it.status }.map { (status, doseCounts) ->
                val colour = statusColours[status] ?: Color.Black
                PieChartData.Slice(
                    status.name,
                    doseCounts.sumOf { it.count }.toFloat(),
                    colour
                )
            }

            val newPieChartData = PieChartData(
                slices = slices,
                plotType = PlotType.Donut
            )

            //App will crash if the donut chart is passed null
            if (newPieChartData.isNotNull()) {
                _donutChartData.value = newPieChartData
                _graphState.value = GraphState.Donut
            } else {
                Log.d("Donut Data", "Entered Null. $_donutChartData")
                _graphState.value = GraphState.EmptyData
            }
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